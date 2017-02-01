package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

/**
 * Created by Lucas on 03/08/2015.
 */
public class CadastroLocalizacaoTask extends AsyncTask<Object, Object, Object> {

    private Activity activity;
    private String data;
    private ProgressDialog progress;
    private Object solicitante;
    private Treinador treinador;
    private Corredor corredor;

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_localizacao_json.php";
    private final String methodTreinador = "cadastroLocalizacaoTreinador-json";
    private final String methodCorredor = "cadastroLocalizacaoCorredor-json";

    public CadastroLocalizacaoTask(Activity activity, Object solicitante) {
        this.solicitante = solicitante;
        this.activity = activity;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object... params) {


        if (solicitante instanceof Treinador) {

            treinador = (Treinador) solicitante;
            TreinadorJson treinadorJson = new TreinadorJson();
            data = treinadorJson.TreinadorToJson(treinador);
            String answer = HttpConnection.getSetDataWeb(this.url, this.methodTreinador, this.data);
            return answer;


        } else if (solicitante instanceof Corredor) {

            corredor = (Corredor) solicitante;
            CorredorJson corredorJson = new CorredorJson();
            data = corredorJson.CorredorToJson(corredor);
            String answer = HttpConnection.getSetDataWeb(this.url, this.methodCorredor, this.data);
            return answer;

        } else {

            String erro = "erro";
            return erro;

        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();
    }
}
