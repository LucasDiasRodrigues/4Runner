package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroTreinoActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinoJson;

/**
 * Created by Lucas on 19/02/2016.
 */
public class CadastraTreinoTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_altera_treino_json.php";
    private String method = "cadastrar_treino-json";

    CadastroTreinoActivity activity;
    Treino treino;

    private ProgressDialog progress;



    public CadastraTreinoTask(CadastroTreinoActivity activity, Treino treino){
        this.treino = treino;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Conectando ao servidor", true, true);
    }




    @Override
    protected Object doInBackground(Object[] params) {

        TreinoJson treinoJson = new TreinoJson();
        String data = treinoJson.TreinoCompletoToJson(treino);


        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Resposta ===", answer);






        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        if(((String)o).contains("sucesso")){

            activity.finish();

        } else{
            Toast.makeText(activity, R.string.opserro, Toast.LENGTH_SHORT);
        }

    }
}
