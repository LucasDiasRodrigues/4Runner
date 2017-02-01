package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.AssociaCorredorTreinadorJson;
import com.team4runner.forrunner.web.HttpConnection;

/**
 * Created by Lucas on 12/08/2015.
 */
public class AssociaTreinadorTask extends AsyncTask <Object,Object,Object> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/associa_treinador_json.php";
    private String method;

    private Activity activity;
    private  String data;
    private ProgressDialog progress;
    private Corredor corredor;
    private Treinador treinador;

    private String substitui;

    public AssociaTreinadorTask(Activity activity, Treinador treinador, Corredor corredor, String method,String substitui){
        this.activity = activity;
        this.corredor = corredor;
        this.treinador = treinador;
        this.method = method;
        this.substitui = substitui;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...","Conectando ao servidor", true, true);
    }

    @Override
    protected Object doInBackground(Object... params) {

        AssociaCorredorTreinadorJson json = new AssociaCorredorTreinadorJson();
        data = json.associaCorredorTreinador(corredor,treinador, substitui);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);

        Log.i("Resposta ===",answer);


        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();
    }
}
