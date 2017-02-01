package com.team4runner.forrunner.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.ChatActivity;
import com.team4runner.forrunner.modelo.Mensagem;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.MensagemJson;

import java.util.List;

/**
 * Created by Lucas on 02/12/2015.
 */
public class ReceberMensagensTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/chat_json.php";
    private final String method = "SolicitaMensagensNew-json";

    private String emailCorredor;
    private String emailTreinador;
    private String leitor;
    private Context context;

    private int page = 1;

    List<Mensagem> mensagens;


    //OLD
    public ReceberMensagensTask(String emailCorredor, String emailTreinador, Context context){
        this.emailCorredor = emailCorredor;
        this.emailTreinador = emailTreinador;
        this.context = context;
    }

    public ReceberMensagensTask(String emailCorredor, String emailTreinador, Context context, int page){
        this.emailCorredor = emailCorredor;
        this.emailTreinador = emailTreinador;
        this.context = context;
        this.page = page;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        SharedPreferences prefs = context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        leitor = prefs.getString("email","");

        MensagemJson mensagemJson = new MensagemJson();
        String data = mensagemJson.SolicitaMensagem(emailCorredor, emailTreinador,leitor, page);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);

        Log.i("testeMensagem",answer);

        mensagens = mensagemJson.JsonArrayToListaMensagem(answer);


        return mensagens;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(page > 1)
            ((ChatActivity)context).updateRecyclerView(mensagens);
        else
            ((ChatActivity)context).atualizarTela(mensagens);


    }
}
