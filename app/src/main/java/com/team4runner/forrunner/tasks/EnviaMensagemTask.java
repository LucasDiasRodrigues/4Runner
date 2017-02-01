package com.team4runner.forrunner.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.ChatActivity;
import com.team4runner.forrunner.modelo.Mensagem;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.MensagemJson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lucas on 01/12/2015.
 */
public class EnviaMensagemTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/chat_json.php";
    private final String method = "EnviaMensagem-json";

    private String emailRemetente;
    private String emailCorredor;
    private String emailTreinador;
    private String msg;
    private Context context;

    private String data;


    public EnviaMensagemTask(String emailRemetente, String emailCorredor, String emailTreinador, String msg, Context context){
        this.emailRemetente = emailRemetente;
        this.emailCorredor = emailCorredor;
        this.emailTreinador = emailTreinador;
        this.msg = msg;
        this.context = context;
    }


    @Override
    protected Object doInBackground(Object[] params) {

        Mensagem mensagem = new Mensagem();
        mensagem.setRemetente(emailRemetente);
        mensagem.setCorredor(emailCorredor);
        mensagem.setTreinador(emailTreinador);
        mensagem.setMsg(msg);
        mensagem.setDthrEnviada(Calendar.getInstance().getTime());

        MensagemJson json = new MensagemJson();
        data = json.MensagemToJson(mensagem);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);

        Log.i("testechat", answer);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        ((ChatActivity)context).receberMensagens();


    }
}
