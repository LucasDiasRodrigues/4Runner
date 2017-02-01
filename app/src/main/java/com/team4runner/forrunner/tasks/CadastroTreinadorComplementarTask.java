package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.TreinadorJson;
import com.team4runner.forrunner.web.HttpConnection;

/**
 * Created by Lucas on 28/06/2015.
 */
public class CadastroTreinadorComplementarTask extends AsyncTask<Object , Object , Object> {

    private Activity activity;
    private  String data;
    private ProgressDialog progress;
    private Treinador treinador;

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_treinador_json.php";
    private final String method = "cadastroComplementarTreinador-json";


    public CadastroTreinadorComplementarTask(Activity activity,Treinador treinador){
        this.treinador = treinador;
        this.activity = activity;

    }



    @Override
    protected void onPreExecute() {

        progress = ProgressDialog.show(activity, "Aguarde...","Envio de dados para web", true, true);


        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... params) {


        TreinadorJson treinadorJson = new TreinadorJson();
        data = treinadorJson.TreinadorToJson(treinador);




        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);




        return answer;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();



        Log.i("resultado Json", o.toString());


        Log.i("resultado Json", o.toString());

        if (o.toString().equals("Sucesso")) {

            SharedPreferences prefs = activity.getSharedPreferences("Configuracoes",activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("logado", true);
            editor.putString("perfil", "treinador");
            editor.putString("nome", treinador.getNome());
            editor.putString("email", treinador.getEmail());
            editor.putString("imagemperfil", treinador.getImagemPerfil());
            editor.commit();

            Log.i("preferencias ==== ",prefs.getString("perfil",""));


            ((CadastroUsuarioActivity) activity).onCadastroFinalizado(treinador);
        } else {
            ((CadastroUsuarioActivity) activity).onErro();
        }
    }
}
