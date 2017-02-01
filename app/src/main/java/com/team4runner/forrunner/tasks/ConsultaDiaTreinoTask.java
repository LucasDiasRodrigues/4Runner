package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.team4runner.forrunner.fragment.HomeCorredorMeuTreinoFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.TreinoExercicio;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinoExercicioJson;

import java.util.List;

/**
 * Created by Lucas on 29/01/2016.
 */
public class ConsultaDiaTreinoTask extends AsyncTask<Object,Object,Object> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/consulta_dia_treino_json.php";
    private String method = "consulta_dia_treino-json";


    private Activity activity;
    private Corredor corredor;
    private Fragment fragment;

   public ConsultaDiaTreinoTask(Activity activity, Corredor corredor, Fragment fragment){
       this.activity = activity;
       this.corredor = corredor;
       this.fragment = fragment;
   }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... params) {
        CorredorJson corredorJson = new CorredorJson();
        String data = corredorJson.CorredorToJson(corredor);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Script", "ANSWER: " + answer);

        TreinoExercicioJson teJson = new TreinoExercicioJson();
        List<TreinoExercicio> mList = teJson.JsonArrayToListaTreinoExercicio(answer);


        return mList;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(fragment != null && fragment instanceof HomeCorredorMeuTreinoFragment){

            ((HomeCorredorMeuTreinoFragment)fragment).listaTreinoDia((List<TreinoExercicio>)o);

        }

    }
}
