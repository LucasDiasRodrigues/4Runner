package com.team4runner.forrunner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.tasks.CadastroCorredorComplementarTask;
import com.team4runner.forrunner.tasks.ListaObjetivosTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 17/06/2015.
 */
public class CadastroCorredorComplementarFragment extends Fragment {

    private Toolbar toolbar;

    private Corredor corredor = new Corredor();

    private EditText etPeso;
    private EditText etAltura;
    private EditText etSobreVc;
    private Button btnAvancar;
    private Spinner spnObj;
    List<Objetivo> objetivos;

    private TextView txtNome;
    private TextView txtemail;
    private ImageView imFoto;
    //private String localArquivoFoto = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_corredor_complementar, container, false);

        toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        ((CadastroUsuarioActivity) getActivity()).setSupportActionBar(toolbar);
        ((CadastroUsuarioActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CadastroUsuarioActivity) getActivity()).getSupportActionBar().setTitle("Cadastro complementar");

        //Popula o objeto corredor com os dados da tela anterior
        if (getArguments() != null) {
            this.corredor = (Corredor) getArguments().getSerializable("corredor");

            //Coloca os dados do Corredor na tela
            txtNome = (TextView) fragment.findViewById(R.id.txtNome);
            txtNome.setText(corredor.getNome());

            txtemail = (TextView) fragment.findViewById(R.id.txtEmail);
            txtemail.setText(corredor.getEmail());

        }


        etPeso = (EditText) fragment.findViewById(R.id.editTextPeso);
        etAltura = (EditText) fragment.findViewById(R.id.editTextAltura);
        etSobreVc = (EditText) fragment.findViewById(R.id.editTextSobreVc);

        spnObj = (Spinner) fragment.findViewById(R.id.spinnerObj);
        configuraSpinner();


        /*
        btnAvancar = (Button) fragment.findViewById(R.id.btnAvancar);
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validaCampos()) {

                    corredor.setPeso(Double.valueOf(etPeso.getText().toString()));
                    corredor.setAltura(Double.valueOf(etAltura.getText().toString()));
                    corredor.setSobreMim(etSobreVc.getText().toString());

                    Objetivo obj = (Objetivo) spnObj.getSelectedItem();
                    corredor.setObjetivo(obj.getCodObj());

                    CadastroCorredorComplementarTask taskCadastro = new CadastroCorredorComplementarTask(getActivity(), corredor);
                    taskCadastro.execute();
                }

            }
        });   */


        return fragment;
    }

    private void configuraSpinner() {

        //pegar dados do server aqui
        ListaObjetivosTask lObjTask = new ListaObjetivosTask(getActivity());
        lObjTask.execute();
        try {
            objetivos = (List) lObjTask.get();


            // String[] objts = new String[];
            ArrayAdapter<Objetivo> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, objetivos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spnObj.setAdapter(adapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public boolean validaCampos() {
        Boolean aux = true;
        if (etPeso.getText().toString().equals("")) {
            etPeso.setError(getResources().getString(R.string.preenchacampo));
            aux = false;
        }
        return aux;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_cadastro_corredor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.avancar) {
            if (validaCampos()) {

                corredor.setPeso(Double.valueOf(etPeso.getText().toString()));
                corredor.setAltura(Double.valueOf(etAltura.getText().toString()));
                corredor.setSobreMim(etSobreVc.getText().toString());

                Objetivo obj = (Objetivo) spnObj.getSelectedItem();
                corredor.setObjetivo(obj.getCodObj());

                CadastroCorredorComplementarTask taskCadastro = new CadastroCorredorComplementarTask(getActivity(), corredor);
                taskCadastro.execute();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
