package com.team4runner.forrunner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.team4runner.forrunner.adapter.ListViewDiaTreinoAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;
import com.team4runner.forrunner.tasks.ListaExerciciosTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiaDeTreinoActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView mListView;
    Button btnVisualizarCorrida;

    Corredor corredor;

    List<Exercicio> listaExercicios = new ArrayList<>();

    ArrayList<TreinoExercicio> diaSelecionado;

    //parametros recebidos
    Treino treino;
    int semana;
    String dia;
    String diaSQL;
    ArrayList<TreinoExercicio> treinoExercicios;

    Treinador treinador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_de_treino);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        treinador = new Treinador();
        treinador.setEmail(prefs.getString("email", ""));
        treinador.setNome(prefs.getString("nome", ""));


        corredor = (Corredor) getIntent().getSerializableExtra("corredor");
        treino = (Treino) getIntent().getSerializableExtra("treino");
        semana = getIntent().getIntExtra("semana", 0);
        dia = getIntent().getStringExtra("dia");
        treinoExercicios = (ArrayList) getIntent().getSerializableExtra("treinoExercicio");
        Log.i("passou??", semana + " " + dia + " " + treinoExercicios.get(0).getExercicio().getCodExercicio());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(treino.getNome());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnVisualizarCorrida = (Button)findViewById(R.id.btnVisualizarCorrida);
        btnVisualizarCorrida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DiaDeTreinoActivity.this, VisualizarCorridaActivity.class);
                intent.putExtra("dia", dia);
                intent.putExtra("corredor", corredor);
                startActivity(intent);


            }
        });

        ListaExercicios();


        mListView = (ListView) findViewById(R.id.list_view);
        ListViewDiaTreinoAdapter adapter = new ListViewDiaTreinoAdapter(this, diaSelecionado);
        mListView.setAdapter(adapter);


        // ----------------------------------------------------------------------------------


    }

    @Override
    protected void onResume() {
        super.onResume();

        Integer aux = diaSelecionado.get(0).getCodCorrida();

        if(aux == 0){

            btnVisualizarCorrida.setClickable(false);
            btnVisualizarCorrida.setBackgroundColor(getResources().getColor(R.color.disable_button));

        }


    }

    public void ListaExercicios() {

        ListaExerciciosTask task = new ListaExerciciosTask(this);
        task.execute();
        try {
            listaExercicios = task.get();
            SelecionaExercicios();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void SelecionaExercicios() {

        diaSelecionado = new ArrayList<>();
        for (TreinoExercicio selecionado : treinoExercicios) {

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String auxData = format.format(selecionado.getData());

           if (selecionado.getSemana() == semana && auxData.equals(dia)) {
                diaSelecionado.add(selecionado);
            }
        }

        for (int i = 0; i < diaSelecionado.size(); i++) {

            for (int j = 0; j < listaExercicios.size(); j++) {
                Log.i("Master123", diaSelecionado.get(i).getExercicio().getCodExercicio() + "");
                if (diaSelecionado.get(i).getExercicio().getCodExercicio() == listaExercicios.get(j).getCodExercicio()) {
                    TreinoExercicio aux = diaSelecionado.get(i);
                    aux.setExercicio(listaExercicios.get(j));
                    diaSelecionado.remove(i);
                    diaSelecionado.add(i,aux);
//                    Log.i("Master", diaSelecionado.get(i).getExercicio().getDescricaoExercicio());
                }

            }


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
