package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.fragment.ExerciciosTreinadorFragment;
import com.team4runner.forrunner.fragment.HomeCorredorMeuTreinoFragment;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.ExercicioJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;
import com.team4runner.forrunner.web.TreinoJson;

import java.util.List;

/**
 * Created by Lucas on 28/07/2015.
 */
public class ListaExerciciosTask extends AsyncTask<Object, Object, List<Exercicio>> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/lista_exercicios_json.php";
    private String method = "lista_exercicios-json";
    private final Context context;
    private String data = "";
    private ProgressDialog progress;
    private Treinador treinador;

    Boolean todos = false;

    ExerciciosTreinadorFragment fragment;
    HomeCorredorMeuTreinoFragment fragment2;

    public ListaExerciciosTask(Context context) {
        this.context = context;
        todos = true;
    }

    public ListaExerciciosTask(Context context, Treinador treinador) {
        this.treinador = treinador;
        this.context = context;
    }

    public ListaExerciciosTask(Context context, ExerciciosTreinadorFragment fragment, Treinador treinador) {
        this.fragment = fragment;
        this.context = context;
        this.treinador = treinador;
    }

    public ListaExerciciosTask(Context context, HomeCorredorMeuTreinoFragment fragment) {
        this.fragment2 = fragment;
        this.context = context;
        todos = true;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List doInBackground(Object... params) {

        if (!todos) {
            TreinadorJson treinadorJson = new TreinadorJson();
            data = treinadorJson.TreinadorToJson(treinador);
        } else {
            data = "";
            method = "lista_todos_exercicios-json";
            Log.i("Exercicios", "Todos: ");
        }

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
        Log.i("Script", "ANSWER: " + answer);

        ExercicioJson json = new ExercicioJson();
        List<Exercicio> exercicios = json.JsonArrayToListaExercicio(answer);


        return exercicios;
    }

    @Override
    protected void onPostExecute(List<Exercicio> exercicios) {
        super.onPostExecute(exercicios);

        if (fragment != null) {

            fragment.atualizaLista(exercicios);

        }

        if (fragment2 != null) {

            fragment2.SelecionaExercicios(exercicios);

        }

    }
}
