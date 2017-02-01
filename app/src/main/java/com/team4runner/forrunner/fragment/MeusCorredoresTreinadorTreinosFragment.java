package com.team4runner.forrunner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team4runner.forrunner.CadastroTreinoActivity;
import com.team4runner.forrunner.HistoricoPorTreinoActivity;
import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
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
 * Created by Lucas on 05/07/2015.
 */
public class MeusCorredoresTreinadorTreinosFragment extends Fragment{

    private ListView mListView;
    List<Treino> mList1 = new ArrayList<>();
    FloatingActionButton fabCadastrarTreino;
    private ProgressBar progress;
    Corredor corredor;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView txtSemTreinos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MeuCorredorTreinadorActivity)getActivity()).setFragmentTreinos(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_meucorredor_treinador_treinos,container,false);

        corredor = ((MeuCorredorTreinadorActivity)getActivity()).getCorredor();

        mListView = (ListView)fragment.findViewById(R.id.list_view_historico);

        progress = (ProgressBar) fragment.findViewById(R.id.progress);

        mSwipeRefreshLayout = (SwipeRefreshLayout) fragment.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary,R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                AtualizaListaTreino();
            }
        });

        txtSemTreinos = (TextView) fragment.findViewById(R.id.txtSemTreinos);


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

        fabCadastrarTreino = (FloatingActionButton)fragment.findViewById(R.id.fabCadastrarTreino);
        fabCadastrarTreino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CadastroTreinoActivity.class);
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



        ListaTreinosTask task = new ListaTreinosTask(getActivity(), corredor, "MeusCorredoresTreinadorTreinosFragment", this, "treinador");
        task.execute();

    }

    public void VerificaAssiduidade(List<Treino> treinos){

        ConsultaAssiduidadeCorredorTask task2 = new ConsultaAssiduidadeCorredorTask(getActivity(), treinos, corredor, "MeusCorredoresTreinadorTreinosFragment", this);
        task2.execute();

    }

    public void AtualizaLista(List<Treino> treinos){

        mList1 = treinos;
        mListView.setAdapter(new ListViewListaTreinosAdapter(getActivity(), mList1, "treinador", corredor));
        progress.setVisibility(View.GONE);

        Log.i("Assiduidade", "Lista concluída");

        if(mList1.size() < 1){
            txtSemTreinos.setVisibility(View.VISIBLE);
        } else {
            txtSemTreinos.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setRefreshing(false);

    }

}
