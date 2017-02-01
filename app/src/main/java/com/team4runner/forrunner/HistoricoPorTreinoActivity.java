package com.team4runner.forrunner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.team4runner.forrunner.adapter.ExpandableListViewHistoricoAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;
import com.team4runner.forrunner.tasks.DetalhaTreinoTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HistoricoPorTreinoActivity extends AppCompatActivity {

    Toolbar toolbar;
    ExpandableListView mExpandableListView;

    List<String> semanas;
    HashMap<String, List<String>> grupo;

    Corredor corredor;
    Treino treino = new Treino();
    private ArrayList<TreinoExercicio> treinoExercicio = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_por_treino);

        treino = (Treino) getIntent().getSerializableExtra("treino");
        corredor = (Corredor) getIntent().getSerializableExtra("corredor");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(treino.getNome());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        detalhaTreino();


        //Impede o erro caso o array retorne vazio do bd
        if (treinoExercicio.size() != 0) {

            mExpandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
            ExpandableListViewHistoricoAdapter adapter = new ExpandableListViewHistoricoAdapter(this, treino, treinoExercicio);
            mExpandableListView.setAdapter(adapter);

            mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    int semanaSelecionada = groupPosition + 1;
                    TextView textViewDia = (TextView) v.findViewById(R.id.textViewDia);
                    String diaSelecionado = textViewDia.getText().toString();

                    Intent intent = new Intent(HistoricoPorTreinoActivity.this, DiaDeTreinoActivity.class);
                    intent.putExtra("semana", semanaSelecionada);
                    intent.putExtra("dia",diaSelecionado);
                    intent.putExtra("treino", treino);
                    intent.putExtra("treinoExercicio", treinoExercicio);
                    intent.putExtra("corredor", corredor);

                    startActivity(intent);

                    return false;
                }
            });

        }


    }



    public void detalhaTreino() {

        DetalhaTreinoTask task = new DetalhaTreinoTask(this, treino);
        task.execute();

        try {
            treinoExercicio = (ArrayList<TreinoExercicio>) task.get();

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
