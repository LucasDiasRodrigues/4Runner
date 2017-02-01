package com.team4runner.forrunner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.team4runner.forrunner.adapter.RecyclerViewAdapterLocalizarCorredoresTreinador;
import com.team4runner.forrunner.adapter.RecyclerViewAdapterLocalizarTreinadorCorredor;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.ListaTreinadoresTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class LocalizarTreinadorActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterLocalizarTreinadorCorredor mAdapter;
    List<Treinador> mListNova = new ArrayList<>();

    private double geoRaio;

    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizar_treinador);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.localizartreinador);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaTreinadores();


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_treinadores);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapterLocalizarTreinadorCorredor(LocalizarTreinadorActivity.this, mListNova, false);
        mRecyclerView.setAdapter(mAdapter);


        progressBar = (ProgressBar) findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary,R.color.blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoLista", "onRefresh called from SwipeRefreshLayout");
                listaTreinadores();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        //Colocar uma mensagem de nada encontrado
        if (mListNova.isEmpty()) {

        }
        listaTreinadores();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_localizar_treinador, menu);
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
                    .setTitle(R.string.localizartreinadorporposicaogeografica)
                    .setMessage(R.string.localizartreinadorporposicaogeografica1)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new AlertDialog.Builder(LocalizarTreinadorActivity.this)
                                    .setTitle(R.string.escolhaRaio)
                                    .setItems(R.array.localizarRaio, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    geoRaio = 5000.0;
                                                    onBuscaTreinadoresPorGeoLocalizacao(geoRaio);
                                                    break;
                                                case 1:
                                                    geoRaio = 10000.0;
                                                    onBuscaTreinadoresPorGeoLocalizacao(geoRaio);
                                                    break;
                                                case 2:
                                                    geoRaio = 50000.0;
                                                    onBuscaTreinadoresPorGeoLocalizacao(geoRaio);
                                                    break;
                                                case 3:
                                                    geoRaio = 100000.0;
                                                    onBuscaTreinadoresPorGeoLocalizacao(geoRaio);
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
        onBuscaTreinadores(newText);

        return false;
    }


    public void listaTreinadores() {

        ListaTreinadoresTask task = new ListaTreinadoresTask(this);
        task.execute();

    }

    public void atualizaListaTreinadores(List<Treinador> treinadores){
        mListNova = treinadores;
        mAdapter = new RecyclerViewAdapterLocalizarTreinadorCorredor(LocalizarTreinadorActivity.this, mListNova, false);
        mRecyclerView.setAdapter(mAdapter);

        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public void onBuscaTreinadores(String s) {

        if (s == null || s.trim().equals("")) {
            onLimparBusca();
            return; // encerra o metodo
        }

        //Cria uma lista com os encontrados
        List<Treinador> treinadoresEncontrados = new ArrayList<>(mListNova);
        for (int i = treinadoresEncontrados.size() - 1; i >= 0; i--) {
            Treinador treinador = treinadoresEncontrados.get(i);
            if (!treinador.getEmail().toUpperCase().contains(s.toUpperCase())) {
                treinadoresEncontrados.remove(treinador);
            }
        }

        mAdapter = new RecyclerViewAdapterLocalizarTreinadorCorredor(this, treinadoresEncontrados, false);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onLimparBusca() {
        mAdapter = new RecyclerViewAdapterLocalizarTreinadorCorredor(this, mListNova, false);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onBuscaTreinadoresPorGeoLocalizacao(double geoRaio) {

        //Definindo a localizacao do corredor
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        String localizacaoCorredor = prefs.getString("localizacao", "");

        //Verifica se o corredor informou uma localizacao
        if (!(localizacaoCorredor.equals("") || localizacaoCorredor.equals("null"))) {

            Log.i("localizacaoCorredor--", localizacaoCorredor);
            String localizacaoCorredorFinal[] = localizacaoCorredor.split(Pattern.quote(","));
            Location locationCorredor = new Location("");
            locationCorredor.setLatitude(Double.valueOf(localizacaoCorredorFinal[0]));
            locationCorredor.setLongitude(Double.valueOf(localizacaoCorredorFinal[1]));


            //Comparando com a localizacao dos treinadores
            List<Treinador> treinadoresGeo = new ArrayList<>(mListNova);
            Location locationTreinador = new Location("");


            for (int i = treinadoresGeo.size() - 1; i >= 0; i--) {
                Treinador treinador = treinadoresGeo.get(i);
                Log.i("logFor", treinador.getNome() + treinador.getLocalizacao());
                //Faz o calculo se o corredor possuir localizacao
                if (!(treinador.getLocalizacao().equals("") || treinador.getLocalizacao().equals("null"))) {
                    String localizacaoTreinadorFinal[] = treinador.getLocalizacao().split(Pattern.quote(","));
                    Log.i("localizacaoTreinador--", localizacaoTreinadorFinal[0]);
                    locationTreinador.setLatitude(Double.valueOf(localizacaoTreinadorFinal[0]));
                    locationTreinador.setLongitude(Double.valueOf(localizacaoTreinadorFinal[1]));
                    if (locationCorredor.distanceTo(locationTreinador) > geoRaio) {
                        treinadoresGeo.remove(treinador);
                    } else {
                        //AdicionaDistancia
                        Log.i("localizacaoTreinador--", localizacaoTreinadorFinal[0]);
                        treinadoresGeo.remove(treinador);
                        treinador.setSituacao(String.valueOf((int) locationCorredor.distanceTo(locationTreinador)));
                        treinadoresGeo.add(treinador);
                    }
                } else {
                    treinadoresGeo.remove(treinador);
                }
            }

            List<Treinador> treinadoresOrdenados = new Treinador().ordenarDistancia(treinadoresGeo);

            mAdapter = new RecyclerViewAdapterLocalizarTreinadorCorredor(this, treinadoresOrdenados, true);
            mRecyclerView.setAdapter(mAdapter);
        } else {

            new AlertDialog.Builder(this).setTitle(R.string.localizarcorredorporposicaogeografica)
                    .setMessage(R.string.localizarporposicaogeograficaerro)
                    .setPositiveButton(R.string.okentendi, null).show();
        }
    }


    public void onTreinadorClicado(Treinador treinadorSelecionado) {

        Intent intent = new Intent(this, VisualizaPerfilTreinadorActivity.class);
        intent.putExtra("Treinador", treinadorSelecionado);
        startActivity(intent);

    }

}
