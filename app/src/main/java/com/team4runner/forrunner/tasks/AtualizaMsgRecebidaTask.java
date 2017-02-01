package com.team4runner.forrunner.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

/**
 * Created by Lucas on 03/12/2015.
 */
public class AtualizaMsgRecebidaTask extends AsyncTask {


    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/chat_json.php";
    private final String methodTreinador = "AtualizaMsgRecebidaTreinador-json";
    private final String methodCorredor = "AtualizaMsgRecebidaCorredor-json";

    private Context context;
    private String email;
    private String origem;
    private boolean chatAtivo;

    private String data;

    public AtualizaMsgRecebidaTask(Context context, String email, String origem, boolean chatAtivo) {
        this.context = context;
        this.email = email;
        this.origem = origem;
        this.chatAtivo = chatAtivo;
    }


    @Override
    protected Object doInBackground(Object[] params) {


        if (origem.equals("treinador")) {

            Treinador treinador = new Treinador();
            treinador.setEmail(email);

            TreinadorJson json = new TreinadorJson();
            data = json.TreinadorToJson(treinador);

            String answer = HttpConnection.getSetDataWeb(this.url, this.methodTreinador, this.data);

            Log.i("consultaMensagens", answer);


            return null;

        } else if (origem.equals("corredor")) {

            Corredor corredor = new Corredor();
            corredor.setEmail(email);

            CorredorJson json = new CorredorJson();
            data = json.CorredorToJson(corredor);

            String answer = HttpConnection.getSetDataWeb(this.url, this.methodCorredor, this.data);

            Log.i("consultaMensagens", answer);


            return null;
        }


        return null;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (chatAtivo) {
            Intent intent = new Intent("chat-4runner");
            //send broadcast
            context.sendBroadcast(intent);


        }

    }
}
