package com.team4runner.forrunner.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team4runner.forrunner.MainTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.adapter.RecyclerViewAdapterHomeCorredoresTreinador;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.ListaCorredoresTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 29/06/2015.
 */
public class HomeTreinadorMeusCorredoresFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterHomeCorredoresTreinador mAdapter;
    List<Corredor> mListNova = new ArrayList<>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView txtSemAlunos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home_treinador_corredores, container, false);

        mRecyclerView = (RecyclerView) fragment.findViewById(R.id.recycler_view_corredores);
        txtSemAlunos = (TextView) fragment.findViewById(R.id.txtSemAlunos);
        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) fragment.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary,R.color.blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                listaCorredores();
            }
        });


        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        listaCorredores();

    }

    public void listaCorredores() {


        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", getActivity().MODE_PRIVATE);
        Treinador treinador = new Treinador();
        treinador.setEmail(prefs.getString("email", ""));

        ListaCorredoresTask task = new ListaCorredoresTask(treinador, getActivity(), "meusCorredores", this);
        task.execute();
    }

    public void atualizaListaCorredores(List<Corredor> corredores){
        mListNova = corredores;
        progressBar.setVisibility(View.GONE);

        if (corredores.size() <= 0){
            txtSemAlunos.setVisibility(View.VISIBLE);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new RecyclerViewAdapterHomeCorredoresTreinador((MainTreinadorActivity)getActivity(), corredores);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            txtSemAlunos.setVisibility(View.GONE);

            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new RecyclerViewAdapterHomeCorredoresTreinador((MainTreinadorActivity)getActivity(), mListNova);
            mRecyclerView.setAdapter(mAdapter);
        }

        mSwipeRefreshLayout.setRefreshing(false);

    }



}
