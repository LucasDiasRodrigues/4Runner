package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinoJson;

/**
 * Created by Lucas on 29/01/2016.
 */
public class AvaliarTreinoCorredorTask extends AsyncTask<String,String,String>{

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_altera_treino_json.php";
    private final String method = "avaliar_treino_corredor-json";

    private ProgressDialog progress;

    private Activity activity;
    private Treino treino;

    public AvaliarTreinoCorredorTask(Activity activity, Treino treino) {
        this.activity = activity;
        this.treino = treino;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected String doInBackground(String... params) {
        TreinoJson treinoJson = new TreinoJson();
        String data = treinoJson.TreinoToJson(treino);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("==SuperResposta =====", answer);


        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        progress.dismiss();

        if (s.equals("sucesso")) {
            Toast.makeText(activity, R.string.avaliacaoalterada, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, R.string.opserro, Toast.LENGTH_SHORT).show();
        }

    }

}
