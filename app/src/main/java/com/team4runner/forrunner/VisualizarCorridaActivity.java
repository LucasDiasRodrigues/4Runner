package com.team4runner.forrunner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.team4runner.forrunner.adapter.AbasPagerAdapterVisualizaCorrida;
import com.team4runner.forrunner.fragment.VisualizaGraficoCorridasFragment;
import com.team4runner.forrunner.fragment.VisualizaRotaCorridasFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.modelo.PontoRota;
import com.team4runner.forrunner.tasks.ConsultaCorridaTask;
import com.team4runner.forrunner.tasks.ConsultaRotaTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VisualizarCorridaActivity extends AppCompatActivity {

    String dia;
    Corredor corredor;

    private String perfil;

    private Corrida corrida;
    private List<PontoRota> pontos = new ArrayList<>();

    private ViewPager mViewPager;
    private int numTabs = 2;
    private TabLayout mTabLayout;

    private Boolean testeDeCampo;
    private Boolean corridaLivre;


    //Fragments Filhas
    private VisualizaRotaCorridasFragment fragRota;
    private VisualizaGraficoCorridasFragment fragGraf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_corrida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes da corrida");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        dia = intent.getStringExtra("dia");
        corredor = (Corredor) intent.getSerializableExtra("corredor");
        testeDeCampo = intent.getBooleanExtra("teste", false);
        corridaLivre = intent.getBooleanExtra("livre", false);

        AbasPagerAdapterVisualizaCorrida pagerAdapter = new AbasPagerAdapterVisualizaCorrida(this, getSupportFragmentManager(), numTabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        if (corridaLivre) {
            corrida = (Corrida) intent.getSerializableExtra("corrida");
            consultaRota();
        } else {
            consultaCorrida();
        }

    }

    public void consultaCorrida() {

        if (testeDeCampo) {

            ConsultaCorridaTask task = new ConsultaCorridaTask(this, corredor.getEmail(), dia, "teste");
            task.execute();

        } else {

            ConsultaCorridaTask task = new ConsultaCorridaTask(this, corredor.getEmail(), dia, "treino");
            task.execute();

        }


    }

    public void setCorrida(Corrida corrida){
        this.corrida = corrida;

        consultaRota();
    }

    public void consultaRota() {
        ConsultaRotaTask task = new ConsultaRotaTask(this, corrida.getCodCorrida());
        task.execute();

    }

    public void setRota(List<PontoRota> pontos){
        this.pontos = pontos;

        //Passar dados para as filhas
        if(fragGraf != null)
            fragGraf.setCorrida(corrida, pontos);

        if(fragRota != null){
            fragRota.setCorrida(corrida, pontos);
        }

    }


    public void setFragRota(VisualizaRotaCorridasFragment fragRota) {
        this.fragRota = fragRota;
    }

    public void setFragGraf(VisualizaGraficoCorridasFragment fragGraf) {
        this.fragGraf = fragGraf;
    }

    public Corrida getCorrida() {
        return this.corrida;
    }

    public List<PontoRota> getRota() {
        return pontos;
    }

    public String getDia() {
        return dia;
    }

    public Corredor getCorredor() {
        return corredor;
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
