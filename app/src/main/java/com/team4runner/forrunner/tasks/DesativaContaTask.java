package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.MainActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

/**
 * Created by Lucas on 18/01/2016.
 */
public class DesativaContaTask extends AsyncTask <Object,String,String>{
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/desativa_conta_json.php";
    private String method;
    private String data;

    private Corredor corredor;
    private Treinador treinador;
    private Context context;
    private String perfil;

    private ProgressDialog progress;

    public DesativaContaTask(Corredor corredor, Context context){
        this.corredor = corredor;
        this.context = context;
        method = "desativa_corredor-json";
        perfil = "corredor";
    }

    public DesativaContaTask(Treinador treinador, Context context){
        this.treinador = treinador;
        this.context = context;
        method = "desativa_treinador-json";
        perfil = "treinador";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);

    }

    @Override
    protected String doInBackground(Object[] params) {

        if(perfil.equals("corredor")){

            CorredorJson corredorJson = new CorredorJson();
            data = corredorJson.CorredorToJson(corredor);

        } else if(perfil.equals("treinador")){
            TreinadorJson treinadorJson = new TreinadorJson();
            data = treinadorJson.TreinadorToJson(treinador);

        }

        String answer = HttpConnection.getSetDataWeb(url, method, data);
        Log.i("HttpConn", answer);


        return answer;
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        progress.dismiss();

        if(o.equals("sucesso")){
            Toast.makeText(context, R.string.contadesativada,Toast.LENGTH_SHORT);

            SharedPreferences prefs = context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        } else {
            Toast.makeText(context, R.string.opserro,Toast.LENGTH_SHORT);
        }


    }
}
