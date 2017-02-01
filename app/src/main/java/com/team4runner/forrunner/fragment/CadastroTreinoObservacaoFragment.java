package com.team4runner.forrunner.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.CadastroTreinoActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;
import com.team4runner.forrunner.tasks.CadastraTreinoTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 18/02/2016.
 */
public class CadastroTreinoObservacaoFragment extends Fragment {

    DateFormat brDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    TextView txtNomeTreino;
    TextView txtObj;
    TextView txtQtdSemana;
    TextView txtPeriodo;
    EditText txtObservacoes;
    Button btnCadastrar;

    CircleImageView circleImageView;
    Corredor corredor;

    Treino treino;
    List<TreinoExercicio> mListTreinoExercicio = new ArrayList<>();

    List<Objetivo> objetivos;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_treino_observacao, container, false);
        setHasOptionsMenu(true);

        //Variaveis da Activity
        treino = ((CadastroTreinoActivity) getActivity()).getTreino();
        mListTreinoExercicio = ((CadastroTreinoActivity) getActivity()).getTreinoExerciciosFinal();

        corredor = ((CadastroTreinoActivity) getActivity()).getCorredor();
        objetivos = ((CadastroTreinoActivity) getActivity()).getListObj();
        preencheCampos(fragment);

        txtObservacoes = (EditText)fragment.findViewById(R.id.txtObservacoes);
        btnCadastrar = (Button)fragment.findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String auxObs = txtObservacoes.getText().toString() + "";
                treino.setObservacao(auxObs);
                treino.setTreinoExercicios(mListTreinoExercicio);

                //Cadastrar Pelo!

                CadastraTreinoTask task = new CadastraTreinoTask((CadastroTreinoActivity)getActivity(),treino);
                task.execute();


            }
        });




        return fragment;
    }

    private void preencheCampos(View fragment){

        txtNomeTreino = (TextView)fragment.findViewById(R.id.txtNomeTreino);
        txtObj = (TextView)fragment.findViewById(R.id.txtObj);
        txtQtdSemana = (TextView)fragment.findViewById(R.id.txtQtdSemanasTreino);
        txtPeriodo = (TextView)fragment.findViewById(R.id.txtPeriodo);
        circleImageView = (CircleImageView)fragment.findViewById(R.id.circleView);

        if (!corredor.getImagemPerfil().equals("")) {
            Picasso.with(getActivity()).load(getActivity().getResources().getString(R.string.imageservermini) + corredor.getImagemPerfil()).into(circleImageView);
        }
        txtNomeTreino.setText(treino.getNome());
        for (Objetivo obj : objetivos){
            if(treino.getObjetivo() == obj.getCodObj()){
                txtObj.setText(obj.getDescricao()+"k");
            }
        }
        txtQtdSemana.setText(String.valueOf(treino.getQtdSemanas()));
        String auxPeriodo = brDateFormat.format(treino.getDtInicio()) + " - " + brDateFormat.format(treino.getDtFim());
        txtPeriodo.setText(auxPeriodo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
