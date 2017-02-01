package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TesteDeCampoJson;
import com.team4runner.forrunner.web.TreinoJson;

/**
 * Created by Lucas on 19/02/2016.
 */
public class CancelarTreinoTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_altera_treino_json.php";
    private  String method = "cancela_treino-json";

    private Activity activity;
    private Treino treino;

    private Boolean sucesso;


    public CancelarTreinoTask(Activity activity, Treino treino){
        this.activity = activity;
        this.treino = treino;
    }



    @Override
    protected Object doInBackground(Object[] params) {

        TreinoJson treinoJson = new TreinoJson();
        String data = treinoJson.TreinoToJson(treino);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Script", "ANSWER: " + answer);


        if(answer.equals("sucesso")){

            sucesso = true;

        } else{
            sucesso = false;
        }




        return sucesso;
    }

    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(sucesso){
            Toast.makeText(activity, R.string.testeCancelado, Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(activity,R.string.opserro,Toast.LENGTH_SHORT).show();
        }
    }

}
