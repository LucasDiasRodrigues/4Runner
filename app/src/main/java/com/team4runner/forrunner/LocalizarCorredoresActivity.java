package com.team4runner.forrunner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.team4runner.forrunner.adapter.RecyclerViewAdapterLocalizarCorredoresTreinador;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.ListaCorredoresTask;
import com.team4runner.forrunner.tasks.ListaObjetivosTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class LocalizarCorredoresActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterLocalizarCorredoresTreinador mAdapter;
    List<Corredor> mListNova = new ArrayList<>();
    Treinador treinador;

    private double geoRaio;

    private List<Objetivo> objetivos;

    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizar_corredores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.localizarcorredores);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        treinador = new Treinador();
        treinador.setEmail(prefs.getString("email", ""));



        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_corredores);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapterLocalizarCorredoresTreinador(this, mListNova,false);
        mRecyclerView.setAdapter(mAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary,R.color.blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                listaCorredors();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        //Colocar uma mensagem de nada encontrado
        if (mListNova.isEmpty()) {

        }

        listaCorredors();
        listaObj();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_localizar_corredores, menu);
        MenuItem searchItem = (MenuItem) menu.findItem(R.id.action_localizar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.email));
        MenuItemCompat.setOnActionExpandListener(searchItem, this);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_localizar_location) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.localizarcorredorporposicaogeografica)
                    .setMessage(R.string.localizarcorredorporposicaogeografica1)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new AlertDialog.Builder(LocalizarCorredoresActivity.this)
                                    .setTitle(R.string.escolhaRaio)
                                    .setItems(R.array.localizarRaio, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    geoRaio = 5000.0;
                                                    onBuscaCorredorsPorGeoLocalizacao(geoRaio);
                                                    break;
                                                case 1:
                                                    geoRaio = 10000.0;
                                                    onBuscaCorredorsPorGeoLocalizacao(geoRaio);
                                                    break;
                                                case 2:
                                                    geoRaio = 50000.0;
                                                    onBuscaCorredorsPorGeoLocalizacao(geoRaio);
                                                    break;
                                                case 3:
                                                    geoRaio = 100000.0;
                                                    onBuscaCorredorsPorGeoLocalizacao(geoRaio);
                                                    break;
                                            }
                                        }
                                    })
                                    .show();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, null)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;// true para expandir a search view
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        onLimparBusca();
        return true; //true para voltar ao tamanho normal
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        onBuscaCorredors(newText);

        return false;
    }


    public void listaCorredors() {

        ListaCorredoresTask task = new ListaCorredoresTask(treinador, this, "procurarCorredores", null);
        task.execute();
    }

    public void atualizaListaCorredores(List<Corredor> corredores){
        mListNova = corredores;
        mAdapter = new RecyclerViewAdapterLocalizarCorredoresTreinador(this, mListNova,false);
        mRecyclerView.setAdapter(mAdapter);

        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

    }


    //filtro por email
    public void onBuscaCorredors(String s) {

        if (s == null || s.trim().equals("")) {
            onLimparBusca();
            return; // encerra o metodo
        }

        //Cria uma lista com os encontrados
        List<Corredor> corredoresEncontrados = new ArrayList<>(mListNova);
        for (int i = corredoresEncontrados.size() - 1; i >= 0; i--) {
            Corredor corredor = corredoresEncontrados.get(i);
            if (!corredor.getEmail().toUpperCase().contains(s.toUpperCase())) {
                corredoresEncontrados.remove(corredor);
            }
        }

        mAdapter = new RecyclerViewAdapterLocalizarCorredoresTreinador(this, corredoresEncontrados,false);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onLimparBusca() {
        mAdapter = new RecyclerViewAdapterLocalizarCorredoresTreinador(this, mListNova, false);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onBuscaCorredorsPorGeoLocalizacao(double geoRaio) {

        //Definindo a localizacao do treinador
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        String localizacaoTreinador = prefs.getString("localizacao", "");

        //Verifica se o treinador informou uma localizacao
        if (!(localizacaoTreinador.equals("") || localizacaoTreinador.equals("null"))) {

            Log.i("localizacaoTreinador--", localizacaoTreinador);
            String localizacaoTreinadorFinal[] = localizacaoTreinador.split(Pattern.quote(","));
            Location locationTreinador = new Location("");
            locationTreinador.setLatitude(Double.valueOf(localizacaoTreinadorFinal[0]));
            locationTreinador.setLongitude(Double.valueOf(localizacaoTreinadorFinal[1]));


            //Comparando com a localizacao do corredor
            List<Corredor> corredoresGeo = new ArrayList<>(mListNova);
            Location locationCorredor = new Location("");
            for (int i = corredoresGeo.size() - 1; i >= 0; i--) {
                Corredor corredor = corredoresGeo.get(i);
                //Faz o calculo se o corredor possuir localizacao
                if (!(corredor.getLocalizacao().equals("") || corredor.getLocalizacao().equals("null"))) {
                    String localizacaoCorredorFinal[] = corredor.getLocalizacao().split(Pattern.quote(","));
                    Log.i("localizacaoCorredor--", localizacaoCorredorFinal[0]);
                    locationCorredor.setLatitude(Double.valueOf(localizacaoCorredorFinal[0]));
                    locationCorredor.setLongitude(Double.valueOf(localizacaoCorredorFinal[1]));
                    if (locationTreinador.distanceTo(locationCorredor) > geoRaio) {
                        corredoresGeo.remove(corredor);
                    } else {
                        //AdicionaDistancia
                        corredoresGeo.remove(corredor);
                        Log.i("LocationDist",String.valueOf(locationTreinador.distanceTo(locationCorredor) ));
                        corredor.setSituacao(String.valueOf((int) locationTreinador.distanceTo(locationCorredor)));
                        corredoresGeo.add(corredor);
                    }
                } else {
                    corredoresGeo.remove(corredor);
                }
            }

            List<Corredor> corredoresOrdenados = new Corredor().ordenarDistancia(corredoresGeo);

            mAdapter = new RecyclerViewAdapterLocalizarCorredoresTreinador(this, corredoresOrdenados, true);
            mRecyclerView.setAdapter(mAdapter);

        } else {

            new AlertDialog.Builder(this).setTitle(R.string.localizarcorredorporposicaogeografica)
                    .setMessage(R.string.localizarporposicaogeograficaerro)
                    .setPositiveButton(R.string.okentendi, null).show();
        }


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
        intent.putExtra("localizarCorredors", true);
        startActivity(intent);

    }

   public void listaObj(){
       //pegar dados do server aqui
       ListaObjetivosTask lObjTask = new ListaObjetivosTask(this);
       lObjTask.execute();
   }

    public void getObj(List<Objetivo> objetivos){
        this.objetivos = objetivos;
    }


}
