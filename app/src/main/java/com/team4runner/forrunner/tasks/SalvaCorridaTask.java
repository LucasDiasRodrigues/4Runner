package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.web.CorridaJson;
import com.team4runner.forrunner.web.HttpConnection;

/**
 * Created by Lucas on 15/10/2015.
 */
public class SalvaCorridaTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/salva_corrida_json.php";
    private final String method = "salva_corrida-json";
    private String data;

    private ProgressDialog progress;

    private Context context;
    private Corrida corrida;



   public SalvaCorridaTask(Context context, Corrida corrida){
       this.context = context;
       this.corrida = corrida;
   }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(context,"Aguarde","enviando dados...", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        CorridaJson json = new CorridaJson();

        data = json.CorridaToJson(corrida);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);

        Log.i("Resposta ===", answer);


        return answer;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();
    }
}
