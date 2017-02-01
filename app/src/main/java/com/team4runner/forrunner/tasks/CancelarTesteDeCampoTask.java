package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TesteDeCampoJson;

/**
 * Created by Lucas on 11/01/2016.
 */
public class CancelarTesteDeCampoTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_teste_json.php";
    private  String method = "cancelaTeste-json";

    private Activity activity;
    private TesteDeCampo testeDeCampo;

    private Boolean sucesso;


    public CancelarTesteDeCampoTask (Activity activity, TesteDeCampo testeDeCampo){
        this.activity = activity;
        this.testeDeCampo = testeDeCampo;
    }


    @Override
    protected Boolean doInBackground(Object[] params) {

        TesteDeCampoJson testeJson = new TesteDeCampoJson();
        String data = testeJson.TesteDeCampoToJson(testeDeCampo);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Script", "ANSWER: " + answer);


        if(answer.equals("sucesso")){

            sucesso = true;

        } else{
            sucesso = false;
        }




        return sucesso;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(sucesso){
            Toast.makeText(activity,R.string.testeCancelado,Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(activity,R.string.opserro,Toast.LENGTH_SHORT).show();
        }
    }
}
