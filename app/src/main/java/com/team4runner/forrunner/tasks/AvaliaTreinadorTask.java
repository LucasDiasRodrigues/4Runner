package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.MainActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.fragment.HomeCorredorTreinadorFragment;
import com.team4runner.forrunner.web.AvaliaTreinadorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.LoginJson;

/**
 * Created by Lucas on 22/03/2016.
 */
public class AvaliaTreinadorTask extends AsyncTask{
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/avalia_treinador_json.php";
    private final String method = "avalia_treinador-json";

    private ProgressDialog progress;


    String emailCorredor;
    String emailTreinador;
    int voto;
    Activity activity;
    DialogFragment fragment;
    HomeCorredorTreinadorFragment parent;


    public AvaliaTreinadorTask (Activity activity, DialogFragment fragment, String emailCorredor, String emailTreinador, int voto, HomeCorredorTreinadorFragment parent){
        this.emailCorredor = emailCorredor;
        this.emailTreinador = emailTreinador;
        this.voto = voto;
        this.activity = activity;
        this.fragment = fragment;
        this.parent = parent;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        AvaliaTreinadorJson json = new AvaliaTreinadorJson();
        String data = json.avaliacaoToJson(emailCorredor,emailTreinador,voto);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("AvaliaTreinadorTask", answer+"");

        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        if(((String)o).equals("sucesso")){
            Toast.makeText(activity, R.string.treinadorAvaliado,Toast.LENGTH_SHORT).show();
            fragment.dismiss();
            parent.atualizaTreinador();
        } else {
            Log.i("AvaliaErro", o.toString());
            Toast.makeText(activity, R.string.opserro,Toast.LENGTH_SHORT).show();
        }

    }
}
