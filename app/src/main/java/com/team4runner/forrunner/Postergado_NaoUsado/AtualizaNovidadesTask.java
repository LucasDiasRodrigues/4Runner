package com.team4runner.forrunner.Postergado_NaoUsado;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

import java.util.List;

/**
 * Created by Lucas on 14/08/2015.
 */
public class AtualizaNovidadesTask extends AsyncTask<Object, Object, Object> {

    private Activity activity;
    private String data;
    private ProgressDialog progress;
    private Treinador treinador;
    private Corredor corredor;

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/atualiza_novidades_json.php";
    private String method;


    public AtualizaNovidadesTask(Activity activity, String method) {
        this.activity = activity;
        this.method = method;
    }


    @Override
    protected Object doInBackground(Object... params) {
        SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        if (prefs.getString("perfil", "").equals("treinador")) {

            treinador = new Treinador();
            treinador.setEmail(prefs.getString("email", ""));
            TreinadorJson jsonTreinador = new TreinadorJson();
            data = jsonTreinador.TreinadorToJson(treinador);

            String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
            Log.i("resposta ===== ", answer);

            if(method.equals("treinador-solicitacao_corredores-json")){

                CorredorJson jsonCorredor = new CorredorJson();
                List<Corredor> mList = jsonCorredor.JsonArrayToListaCorredor(answer);
                return mList;
            }

            return answer;

        }

        String erro = "erro";
        return erro;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
