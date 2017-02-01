package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team4runner.forrunner.HistoricoPorTreinoActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.adapter.ListViewListaTreinosAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.tasks.ConsultaAssiduidadeCorredorTask;
import com.team4runner.forrunner.tasks.ListaTreinosTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 14/06/2015.
 */
public class HistoricoTreinosCorredorFragment extends Fragment {


    private ListView mListView;
    List<Treino> mList1 = new ArrayList<>();
    Corredor corredor;

    private ProgressBar progress;
    private TextView txtSemTreinos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_historico_treinos_corredor,container,false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        corredor = new Corredor();
        corredor.setEmail(prefs.getString("email", ""));
        corredor.setNome(prefs.getString("nome", ""));
        corredor.setImagemPerfil(prefs.getString("imagemPerfil", ""));
        corredor.setEmailTreinador(prefs.getString("emailTreinador", ""));

        progress = (ProgressBar) fragment.findViewById(R.id.progress);

        txtSemTreinos = (TextView) fragment.findViewById(R.id.txtSemTreinos);

        mListView = (ListView)fragment.findViewById(R.id.list_view_historico);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //teste
                Treino treinoSelecionado = (Treino)parent.getItemAtPosition(position);


                Intent intent = new Intent(getActivity(), HistoricoPorTreinoActivity.class);
                intent.putExtra("treino",treinoSelecionado);
                intent.putExtra("corredor", corredor);
                startActivity(intent);

            }
        });

        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();

        AtualizaListaTreino();

    }

    public void AtualizaListaTreino(){

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        corredor = new Corredor();
        corredor.setEmail(prefs.getString("email", ""));
        corredor.setEmailTreinador(prefs.getString("emailTreinador", ""));
        corredor.setNome(prefs.getString("nome", ""));

        ListaTreinosTask task = new ListaTreinosTask(getActivity(), corredor, "HistoricoTreinosCorredorFragment", this, "corredor");
        task.execute();

    }

    public void VerificaAssiduidade(List<Treino> treinos){

        ConsultaAssiduidadeCorredorTask task2 = new ConsultaAssiduidadeCorredorTask(getActivity(), treinos, corredor, "HistoricoTreinosCorredorFragment", this);
        task2.execute();

    }

    public void AtualizaLista(List<Treino> treinos){

        mList1 = treinos;
        mListView.setAdapter(new ListViewListaTreinosAdapter(getActivity(), mList1, "corredor", corredor));
        progress.setVisibility(View.GONE);

        if(mList1.size() < 1){
            txtSemTreinos.setVisibility(View.VISIBLE);
        } else {
            txtSemTreinos.setVisibility(View.GONE);
        }

    }

}
