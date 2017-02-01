package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.web.CorridaJson;
import com.team4runner.forrunner.web.HttpConnection;

/**
 * Created by Lucas on 02/02/2016.
 */
public class VerificaUltimaCorridaTreinoTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/consulta_ultima_corrida_treino_json.php";
    private String method = "consulta_ultima_corrida-json";

    private ProgressDialog progress;

    private Activity activity;
    private Corrida corrida;

    public VerificaUltimaCorridaTreinoTask(Activity activity, Corrida corrida){
        this.activity = activity;
        this.corrida = corrida;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Obtendo dados da web", true, true);
    }


    @Override
    protected Object doInBackground(Object[] params) {

        CorridaJson corridaJson = new CorridaJson();
        String data = corridaJson.CorridaToJson(corrida);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Script", "ANSWER lastRun: " + answer);

        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();
    }
}
