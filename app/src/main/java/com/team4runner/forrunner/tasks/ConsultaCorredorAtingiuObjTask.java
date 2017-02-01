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
public class ConsultaCorredorAtingiuObjTask extends AsyncTask<Object, String, String> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/consulta_corredor_atingiu_obj_json.php";
    private String method = "consulta_atingiu_obj-json";


    private Activity activity;
    private Corrida corrida;

    private ProgressDialog progress;

    public ConsultaCorredorAtingiuObjTask(Activity activity, Corrida corrida) {
        this.corrida = corrida;
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Obtendo dados da web", true, true);
    }

    @Override
    protected String doInBackground(Object... params) {

        CorridaJson corridaJson = new CorridaJson();
        String data = corridaJson.CorridaToJson(corrida);
        Log.i("Script", "Request MAXIMO: " + data);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Script", "ANSWER MAXIMA: " + answer);

        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progress.dismiss();


    }
}
