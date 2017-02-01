package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;

/**
 * Created by Lucas on 21/01/2016.
 */
public class DesassociarTask extends AsyncTask<Object, String, String> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/desassocia_json.php";
    private String method = "desassocia-json";
    private String data;

    private Activity activity;
    private Corredor corredor;

    private String perfil;


    public DesassociarTask(Activity activity, Corredor corredor, Treinador treinador) {
        this.activity = activity;
        this.corredor = corredor;

        this.corredor.setEmailTreinador(treinador.getEmail());
        perfil = "treinador";
    }

    public DesassociarTask(Activity activity, Corredor corredor) {
        this.activity = activity;
        this.corredor = corredor;
        perfil = "corredor";
    }


    @Override
    protected String doInBackground(Object... params) {


        CorredorJson corredorJson = new CorredorJson();
        data = corredorJson.CorredorToJson(corredor);


        String answer = HttpConnection.getSetDataWeb(url, method, data);
        Log.i("HttpConn", answer);


        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        if (perfil.equals("corredor")) {
            switch (s){
                case "sucesso":

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("emailTreinador",null);
                    editor.commit();
                    Log.i("LogDesassocia",prefs.getString("emailTreinador","vyugbgeir"));

                    new AlertDialog.Builder(activity).setTitle(R.string.desassociado)
                            .setMessage(R.string.desassociadocorredortexto)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                    activity.startActivity(activity.getIntent());
                                }
                            })
                            .show();
                    break;
                default:
                    Toast.makeText(activity, R.string.opserro,Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (perfil.equals("treinador")){

            switch (s){
                case "sucesso":

                    new AlertDialog.Builder(activity).setTitle(R.string.desassociado)
                            .setMessage(R.string.desassociadotreinadortexto)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            })
                            .show();
                    break;
                default:
                    Toast.makeText(activity, R.string.opserro,Toast.LENGTH_SHORT).show();
                    break;
            }

        }





    }
}
