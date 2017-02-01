package com.team4runner.forrunner;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.team4runner.forrunner.adapter.RecyclerViewAdapterNovosCorredoresTreinador;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.tasks.ListaObjetivosTask;
import com.team4runner.forrunner.tasks.NovosCorredoresTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NovosCorredoresActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterNovosCorredoresTreinador mAdapter;
    List<Corredor> mList = new ArrayList<>();
    private List<Objetivo> objetivos;

    private TextView txtSemCorredores;
    private TextView txtTitulo;
    private TextView txtSubTitulo;

    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novos_corredores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Novos Corredores");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NovosCorredoresActivity.this, LocalizarCorredoresActivity.class);
                startActivity(intent);


            }
        });
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_novos_corredores);
        mRecyclerView.setHasFixedSize(true);


        progressBar = (ProgressBar) findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary,R.color.blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                listaCorredores();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        //CancelaNotifications se houver
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel("solicitacaoAssociacaoCorredor", 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        listaCorredores();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mList.clear();
    }



    public void listaCorredores(){
        NovosCorredoresTask task = new NovosCorredoresTask(this,"treinador-solicitacao_corredores-json");
        task.execute();

    }


    public void atualizaListaCorredores(List<Corredor> corredores){
        mList = corredores;

        listaObj();

        progressBar.setVisibility(View.GONE);

        txtSemCorredores = (TextView) findViewById(R.id.txtSemCorredores);
        txtTitulo = (TextView)findViewById(R.id.titulo);
        txtSubTitulo = (TextView) findViewById(R.id.subtitulo);

        if (corredores.size() <= 0){

            txtSemCorredores.setVisibility(View.VISIBLE);
            txtTitulo.setVisibility(View.GONE);
            txtSubTitulo.setVisibility(View.GONE);

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new RecyclerViewAdapterNovosCorredoresTreinador(this, mList,"NovosCorredores");
            mRecyclerView.setAdapter(mAdapter);
        } else {

            txtSemCorredores.setVisibility(View.GONE);
            txtTitulo.setVisibility(View.VISIBLE);
            txtSubTitulo.setVisibility(View.VISIBLE);

            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new RecyclerViewAdapterNovosCorredoresTreinador(this, mList, "NovosCorredores");
            mRecyclerView.setAdapter(mAdapter);
        }

        mSwipeRefreshLayout.setRefreshing(false);

    }

    public void onCorredorClicado(Corredor corredorSelecionado) {

        String descricaoObj = "";

        for(Objetivo obj : objetivos){
            if(obj.getCodObj() == corredorSelecionado.getObjetivo()){
                descricaoObj = obj.getDescricao();
            }
        }

        Intent intent = new Intent(this, VisualizaPerfilCorredorActivity.class);
        intent.putExtra("Corredor", corredorSelecionado);
        intent.putExtra("obj", descricaoObj);
        intent.putExtra("localizarCorredors", false);
        startActivity(intent);

    }

    public void listaObj(){

        //pegar dados do server aqui
        ListaObjetivosTask lObjTask = new ListaObjetivosTask(this);
        lObjTask.execute();

    }

    public void getListObj(List listObj){
        objetivos = listObj;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
