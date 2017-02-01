package com.team4runner.forrunner.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.team4runner.forrunner.fragment.HomeCorredorTreinadorFragment;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;

/**
 * Created by Lucas on 16/12/2015.
 */
public class ConsultarPerfilTreinadorTask extends AsyncTask {


    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/consulta_perfil_treinador_json.php";
    private final String method = "consulta_treinador-json";
    private Context context;
    private String emailTreinador;
    private String emailCorredor;
    private Fragment fragment;


    public ConsultarPerfilTreinadorTask(Context context, String emailTreinador, String emailCorredor, Fragment fragment) {
        this.context = context;
        this.emailTreinador = emailTreinador;
        this.emailCorredor = emailCorredor;
        this.fragment = fragment;
    }


    @Override
    protected Object doInBackground(Object[] params) {

        Treinador treinador = new Treinador();
        treinador.setEmail(emailTreinador);
        CorredorJson json = new CorredorJson();
        String data = json.ConsutaTreinadorToJson(emailCorredor, emailTreinador);

        String answer = HttpConnection.getSetDataWeb(url, method, data);

        Log.i("HttpConn", answer);

        if (!answer.equals("")) {
            Bundle bundle = json.JsonConsutaTreinadorToBundle(answer);
            return bundle;
        }

        return "";
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (!o.equals("")) {
            ((HomeCorredorTreinadorFragment) fragment).atualizaInterfaceTreinador((Bundle) o);
        }
    }
}
