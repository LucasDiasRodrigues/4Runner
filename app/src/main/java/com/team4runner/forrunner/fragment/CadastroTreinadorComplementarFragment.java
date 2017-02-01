package com.team4runner.forrunner.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.CadastroCorredorComplementarTask;
import com.team4runner.forrunner.tasks.CadastroTreinadorComplementarTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 28/06/2015.
 */
public class CadastroTreinadorComplementarFragment extends Fragment {

    private Toolbar toolbar;

    private Treinador treinador = new Treinador();

    private EditText etCref;
    private EditText etFormacao;
    private EditText etSobreVc;
    private Button btnAvancar;

    private TextView txtNome;
    private TextView txtemail;
    private CircleImageView imFoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_treinador_complementar, container, false);

        toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        ((CadastroUsuarioActivity) getActivity()).setSupportActionBar(toolbar);
        ((CadastroUsuarioActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CadastroUsuarioActivity) getActivity()).getSupportActionBar().setTitle("Cadastro complementar");

        //Popula o objeto treinador com os dados da tela anterior
        if (getArguments() != null) {
            this.treinador = (Treinador) getArguments().getSerializable("treinador");

            //Coloca os dados do Treinador na tela
            txtNome = (TextView) fragment.findViewById(R.id.txtNome);
            txtNome.setText(treinador.getNome());

            txtemail = (TextView) fragment.findViewById(R.id.txtEmail);
            txtemail.setText(treinador.getEmail());
        }

        etCref = (EditText) fragment.findViewById(R.id.editTextCref);
        etFormacao = (EditText) fragment.findViewById(R.id.editTextFormacao);
        etSobreVc = (EditText) fragment.findViewById(R.id.editTextSobreVc);

        /*
        btnAvancar = (Button) fragment.findViewById(R.id.btnAvancar);
        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validaCampos()) {

                    treinador.setCref(Double.valueOf(etCref.getText().toString()));
                    treinador.setFormacao(etFormacao.getText().toString());
                    treinador.setSobreMim(etSobreVc.getText().toString());


                    CadastroTreinadorComplementarTask taskCadastro = new CadastroTreinadorComplementarTask(getActivity(), treinador);
                    taskCadastro.execute();
                }

            }
        }); */

        return fragment;
    }

    public boolean validaCampos() {
        Boolean aux = true;

        if (etCref.getText().toString().equals("")) {
            etCref.setError(getResources().getString(R.string.preenchacampo));
            aux = false;
        }

        if (etFormacao.getText().toString().equals("")) {
            etFormacao.setError(getResources().getString(R.string.preenchacampo));
            aux = false;
        }


        return aux;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_cadastro_treinador, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.avancar) {
            if (validaCampos()) {

                treinador.setCref(etCref.getText().toString());
                treinador.setFormacao(etFormacao.getText().toString());
                treinador.setSobreMim(etSobreVc.getText().toString());

                CadastroTreinadorComplementarTask taskCadastro = new CadastroTreinadorComplementarTask(getActivity(), treinador);
                taskCadastro.execute();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
