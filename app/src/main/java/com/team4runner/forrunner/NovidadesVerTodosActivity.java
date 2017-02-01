package com.team4runner.forrunner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.team4runner.forrunner.adapter.ListViewAdapterNovidades;
import com.team4runner.forrunner.tasks.ListaNovidadesTreinadorTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NovidadesVerTodosActivity extends AppCompatActivity {

    private ListView listViewNovidades;
    String metodo = "";
    String titulo = "";


    private String emailTreinador;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novidades_ver_todos);

        Intent intent = getIntent();
        metodo = intent.getStringExtra("lista");
        titulo = intent.getStringExtra("titulo");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titulo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        emailTreinador = prefs.getString("email", "");

        listViewNovidades = (ListView)findViewById(R.id.listViewNovidades);

    }

    @Override
    protected void onResume() {
        super.onResume();

        atualizaNovidades();

    }

    private void atualizaNovidades(){

        ListaNovidadesTreinadorTask task = new ListaNovidadesTreinadorTask(this, emailTreinador, metodo, true);
        task.execute();

        try {
            List<Bundle> bundles = (List<Bundle>) task.get();

            ListViewAdapterNovidades adapterNovidades = new ListViewAdapterNovidades(this, bundles, metodo);
            listViewNovidades.setAdapter(adapterNovidades);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }






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
