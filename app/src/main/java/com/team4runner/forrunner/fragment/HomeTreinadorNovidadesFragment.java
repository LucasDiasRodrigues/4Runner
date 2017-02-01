package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.team4runner.forrunner.NovidadesVerTodosActivity;
import com.team4runner.forrunner.R;

import com.team4runner.forrunner.adapter.ListViewAdapterNovidades;
import com.team4runner.forrunner.tasks.ListaNovidadesTreinadorTask;

import java.util.List;

/**
 * Created by Lucas on 29/06/2015.
 */
public class HomeTreinadorNovidadesFragment extends Fragment {

    private ListView listViewTreinosaVencer;
    private ListView listViewTreinosConcluidos;
    private ListView listViewUltimasCorridas;
    private ListView listViewUltimosTestes;
    private ListView listViewNovosCorredores;
    private ListView listViewAusencia;
    private ListView listViewAusenciaTeste;

    private Button btnVerTodosCard1;
    private Button btnVerTodosCard2;
    private Button btnVerTodosCard3;
    private Button btnVerTodosCard4;
    private Button btnVerTodosCard5;
    private Button btnVerTodosCard6;
    private Button btnVerTodosCard7;

    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;
    private CardView card5;
    private CardView card6;
    private CardView card7;

    private String emailTreinador;

    private ProgressBar progress;
    private TextView txtSemNovidades;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int countTasks = 0;
    private boolean cardAtivo = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        emailTreinador = prefs.getString("email", "");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home_treinador_novidades, container, false);

        progress = (ProgressBar) fragment.findViewById(R.id.progress);


        txtSemNovidades = (TextView) fragment.findViewById(R.id.txtSemNovidades);

        card1 = (CardView) fragment.findViewById(R.id.card1);
        card2 = (CardView) fragment.findViewById(R.id.card2);
        card3 = (CardView) fragment.findViewById(R.id.card3);
        card4 = (CardView) fragment.findViewById(R.id.card4);
        card5 = (CardView) fragment.findViewById(R.id.card5);
        card6 = (CardView) fragment.findViewById(R.id.card6);
        card7 = (CardView) fragment.findViewById(R.id.card7);


        listViewTreinosaVencer = (ListView) fragment.findViewById(R.id.listViewTreinosAVencer);
        listViewTreinosConcluidos = (ListView) fragment.findViewById(R.id.listViewTreinosConcluidos);
        listViewUltimasCorridas = (ListView) fragment.findViewById(R.id.listViewUltimasCoridas);
        listViewUltimosTestes = (ListView) fragment.findViewById(R.id.listViewUltimosTestes);
        listViewNovosCorredores = (ListView) fragment.findViewById(R.id.listViewNovosCorredores);
        listViewAusencia = (ListView) fragment.findViewById(R.id.listViewAusencia);
        listViewAusenciaTeste = (ListView) fragment.findViewById(R.id.listViewAusenciaTeste);

        btnVerTodosCard1 = (Button) fragment.findViewById(R.id.btnVerTodosCard1);
        btnVerTodosCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NovidadesVerTodosActivity.class);
                intent.putExtra("lista", "treinoavencer");
                intent.putExtra("titulo", "Treinos a vencer");

                startActivity(intent);

            }
        });
        btnVerTodosCard2 = (Button) fragment.findViewById(R.id.btnVerTodosCard2);
        btnVerTodosCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NovidadesVerTodosActivity.class);
                intent.putExtra("lista", "treinoconcluido");
                intent.putExtra("titulo", "Treinos Concluidos");
                startActivity(intent);
            }
        });
        btnVerTodosCard3 = (Button) fragment.findViewById(R.id.btnVerTodosCard3);
        btnVerTodosCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NovidadesVerTodosActivity.class);
                intent.putExtra("lista", "ultimascorridas");
                intent.putExtra("titulo", "Ultimas Corridas");
                startActivity(intent);
            }
        });
        btnVerTodosCard4 = (Button) fragment.findViewById(R.id.btnVerTodosCard4);
        btnVerTodosCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NovidadesVerTodosActivity.class);
                intent.putExtra("lista", "ultimostestes");
                intent.putExtra("titulo", "Ultimos testes de campo");
                startActivity(intent);
            }
        });
        btnVerTodosCard5 = (Button) fragment.findViewById(R.id.btnVerTodosCard5);
        btnVerTodosCard5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NovidadesVerTodosActivity.class);
                intent.putExtra("lista", "novoscorredores");
                intent.putExtra("titulo", "Novos corredores");
                startActivity(intent);
            }
        });
        btnVerTodosCard6 = (Button) fragment.findViewById(R.id.btnVerTodosCard6);
        btnVerTodosCard6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NovidadesVerTodosActivity.class);
                intent.putExtra("lista", "ausencias");
                intent.putExtra("titulo", "Corridas de treino nao realizadas");
                startActivity(intent);
            }
        });
        btnVerTodosCard7 = (Button) fragment.findViewById(R.id.btnVerTodosCard7);
        btnVerTodosCard7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NovidadesVerTodosActivity.class);
                intent.putExtra("lista", "ausenciasteste");
                intent.putExtra("titulo", "Testes de campo nao realizados");
                startActivity(intent);
            }
        });

        progress.setVisibility(View.VISIBLE);


        mSwipeRefreshLayout = (SwipeRefreshLayout) fragment.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary,R.color.blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoCards", "onRefresh called from SwipeRefreshLayout");
                atualizaNovidades();
            }
        });


        /*/Anunciozinho maroto
        AdView mAdView = (AdView) fragment.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("14C364ED1292A3EA3A72FD44B0EBBC24").build();
        mAdView.loadAd(adRequest);

*/

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();


        new Thread() {
            public void run() {
                try {
                    Thread.sleep(250);
                    atualizaNovidades();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();




    }

    public void atualizaNovidades() {

        //1
        ListaNovidadesTreinadorTask task = new ListaNovidadesTreinadorTask(getActivity(), emailTreinador, "treinoavencer", false, this);
        task.execute();

        //2
        ListaNovidadesTreinadorTask task2 = new ListaNovidadesTreinadorTask(getActivity(), emailTreinador, "treinoconcluido", false, this);
        task2.execute();

        //3
        ListaNovidadesTreinadorTask task3 = new ListaNovidadesTreinadorTask(getActivity(), emailTreinador, "ultimascorridas", false, this);
        task3.execute();

        //4
        ListaNovidadesTreinadorTask task4 = new ListaNovidadesTreinadorTask(getActivity(), emailTreinador, "ultimostestes", false, this);
        task4.execute();

        //5
        ListaNovidadesTreinadorTask task5 = new ListaNovidadesTreinadorTask(getActivity(), emailTreinador, "novoscorredores", false, this);
        task5.execute();

        //6
        ListaNovidadesTreinadorTask task6 = new ListaNovidadesTreinadorTask(getActivity(), emailTreinador, "ausencias", false, this);
        task6.execute();

        //7
        ListaNovidadesTreinadorTask task7 = new ListaNovidadesTreinadorTask(getActivity(), emailTreinador, "ausenciasteste", false, this);
        task7.execute();

    }

    public void atualizaTreinosaVencer(List<Bundle> bundles) {

            countTasks++;

            if(bundles.size()>0){
                cardAtivo = true;
                card1.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                ListViewAdapterNovidades adapterNovidades = new ListViewAdapterNovidades(getActivity(), bundles, "treinoavencer");
                listViewTreinosaVencer.setAdapter(adapterNovidades);
            }

            if (countTasks >=7 && !cardAtivo){

                txtSemNovidades.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            } else {

                txtSemNovidades.setVisibility(View.GONE);
            }

    }


    public void atualizaTreinosConcluidos(List<Bundle> bundles) {

            countTasks++;

            if(bundles.size()>0){
                cardAtivo = true;
                card2.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                ListViewAdapterNovidades adapterNovidades = new ListViewAdapterNovidades(getActivity(), bundles, "treinoconcluido");
                listViewTreinosConcluidos.setAdapter(adapterNovidades);
            }

            if (countTasks >=7 && !cardAtivo){

                txtSemNovidades.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }else {

                txtSemNovidades.setVisibility(View.GONE);

            }

    }

    public void atualizaUltimasCorridas(List<Bundle> bundles) {

            countTasks++;

            if(bundles.size()>0){
                cardAtivo = true;
                card3.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

                ListViewAdapterNovidades adapterNovidades = new ListViewAdapterNovidades(getActivity(), bundles, "ultimascorridas");
                listViewUltimasCorridas.setAdapter(adapterNovidades);
            }

            if (countTasks >=7 && !cardAtivo){
                txtSemNovidades.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

            }else {

                txtSemNovidades.setVisibility(View.GONE);

            }

    }

    public void atualizaUltimosTestes(List<Bundle> bundles) {

            countTasks++;

            if(bundles.size()>0){
                cardAtivo = true;
                card4.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

                ListViewAdapterNovidades adapterNovidades = new ListViewAdapterNovidades(getActivity(), bundles, "ultimostestes");
                listViewUltimosTestes.setAdapter(adapterNovidades);
            }

            if (countTasks >=7 && !cardAtivo){
                txtSemNovidades.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

            }else {

                txtSemNovidades.setVisibility(View.GONE);

            }

    }

    public void atualizaNovosCorredores(List<Bundle> bundles) {

            countTasks++;

            if(bundles.size()>0){
                cardAtivo = true;
                card5.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                Log.i("chama o adapeter", "novos corredores");
                ListViewAdapterNovidades adapterNovidades = new ListViewAdapterNovidades(getActivity(), bundles, "novoscorredores");
                listViewNovosCorredores.setAdapter(adapterNovidades);
            }


            if (countTasks >=7 && !cardAtivo){

                txtSemNovidades.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }else {

                txtSemNovidades.setVisibility(View.GONE);

            }

    }

    public void atualizaListaAusencia(List<Bundle> bundles) {

            countTasks++;

            if(bundles.size()>0){
                cardAtivo = true;
                card6.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                Log.i("chama o adapeter", "ausencias");
                ListViewAdapterNovidades adapterNovidades = new ListViewAdapterNovidades(getActivity(), bundles, "ausencias");
                listViewAusencia.setAdapter(adapterNovidades);
            }


            if (countTasks >=7 && !cardAtivo){
                txtSemNovidades.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

            }else {

                txtSemNovidades.setVisibility(View.GONE);

            }

    }

    public void atualizaListaAusenciaTeste(List<Bundle> bundles) {

            countTasks++;

            if(bundles.size()>0){
                cardAtivo = true;
                card7.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                ListViewAdapterNovidades adapterNovidades = new ListViewAdapterNovidades(getActivity(), bundles, "ausenciasteste");
                listViewAusenciaTeste.setAdapter(adapterNovidades);
            }

            if (countTasks >=7 && !cardAtivo){

                txtSemNovidades.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

            }else {

                txtSemNovidades.setVisibility(View.GONE);

            }

        mSwipeRefreshLayout.setRefreshing(false);

    }




}
