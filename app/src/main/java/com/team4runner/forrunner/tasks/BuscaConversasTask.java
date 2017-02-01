package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.team4runner.forrunner.ConversasActivity;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.MensagemJson;
import com.team4runner.forrunner.web.TreinadorJson;

import java.util.List;

/**
 * Created by Lucas on 22/01/2016.
 */
public class BuscaConversasTask extends AsyncTask<Object, Object, Object> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/chat_json.php";
    private String method;
    private String data;

    private Activity activity;
    private Corredor corredor;
    private Treinador treinador;

    private String perfil;

    private ProgressDialog progress;


    public BuscaConversasTask(Activity activity, Corredor corredor) {
        this.activity = activity;
        this.corredor = corredor;
        perfil = "corredor";
    }

    public BuscaConversasTask(Activity activity, Treinador treinador) {
        this.activity = activity;
        this.treinador = treinador;
        perfil = "treinador";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Bundle> doInBackground(Object... params) {

        if(perfil.equals("corredor")){
            CorredorJson corredorJson = new CorredorJson();
            data = corredorJson.CorredorToJson(corredor);
            method = "busca_conversas_por_corredor-json";
        } else if (perfil.equals("treinador")){
            TreinadorJson treinadorJson = new TreinadorJson();
            data = treinadorJson.TreinadorToJson(treinador);
            method = "busca_conversas_por_treinador-json";
        }

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
        Log.i("Resposta ===", answer);

        MensagemJson msgJson = new MensagemJson();
        List<Bundle> bundles = msgJson.JsonToListaConversas(answer);

        return bundles;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        ((ConversasActivity)activity).atualizaLista((List<Bundle>)o);



    }
}
