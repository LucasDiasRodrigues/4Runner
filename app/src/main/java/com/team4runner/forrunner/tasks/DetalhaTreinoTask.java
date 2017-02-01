package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinoExercicioJson;
import com.team4runner.forrunner.web.TreinoJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 21/08/2015.
 */
public class DetalhaTreinoTask extends AsyncTask<Object,Object,Object> {
    private Activity activity;
    private Treino treino;

    List<TreinoExercicio> treinoexercicio = new ArrayList<>();

    private ProgressDialog progress;

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/detalha_treino_json.php";
    private final String method = "detalha_treino-json";


    public DetalhaTreinoTask(Activity activity, Treino treino){
        this.activity = activity;
        this.treino = treino;
    }



    @Override
    protected Object doInBackground(Object... params) {

        TreinoJson json = new TreinoJson();
        String jsonTreino = json.TreinoToJson(treino);

        Log.i("RequisicaoDetalheTreino",jsonTreino);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonTreino);

        Log.i("RespostaDetalheTreino =",answer);

        TreinoExercicioJson tEJson = new TreinoExercicioJson();

        treinoexercicio = (List)tEJson.JsonArrayToListaTreinoExercicio(answer);



        return treinoexercicio;
    }


}
