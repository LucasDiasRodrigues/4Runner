package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.MainCorredorActivity;
import com.team4runner.forrunner.MainTreinadorActivity;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.LoginJson;

/**
 * Created by Lucas on 27/04/2016.
 */
public class VerificaNotificacoesTask extends AsyncTask {


    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/verifica_notificacoes_json.php";
    private String method = "";
    private Activity activity;

    private String perfil;
    private String email;

    public VerificaNotificacoesTask(MainCorredorActivity activity, String email) {
        this.activity = activity;
        method = "verifica_notific_corredor-json";
        perfil = "corredor";
        this.email = email;
    }

    public VerificaNotificacoesTask(MainTreinadorActivity activity, String email) {
        this.activity = activity;
        method = "verifica_notific_treinador-json";
        perfil = "treinador";
        this.email = email;
    }


    @Override
    protected Object doInBackground(Object[] params) {

        LoginJson json = new LoginJson();
        String data = json.logoffToJson(email , perfil);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("resposta == ", answer);

        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (perfil.equals("corredor")) {
            if (o.toString().equals("msg-sim")) {
                ((MainCorredorActivity)activity).onNovaMensagem(true);
            } else if (o.toString().equals("msg-nao")) {
                ((MainCorredorActivity)activity).onNovaMensagem(false);
            }


        } else if (perfil.equals("treinador")) {

            if (o.toString().contains("msg-sim")) {
                ((MainTreinadorActivity)activity).onNovaMensagem(true);
            } else if (o.toString().contains("msg-nao")) {
                ((MainTreinadorActivity)activity).onNovaMensagem(false);
            }


            if (o.toString().contains("slc-sim")) {
                ((MainTreinadorActivity)activity).onNovaSolicitacao(true);
            } else if (o.toString().contains("slc-nao")) {
                ((MainTreinadorActivity)activity).onNovaSolicitacao(false);
            }
        }


    }
}
