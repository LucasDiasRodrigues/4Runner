package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;

/**
 * Created by Lucas on 24/06/2015.
 */
public class CadastroCorredorComplementarTask extends AsyncTask <Object , Object , Object> {


    private Activity activity;
    private  String data;
    private ProgressDialog progress;
    private Corredor corredor;

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_corredor_json.php";
    private final String method = "cadastroComplementarCorredor-json";


    public CadastroCorredorComplementarTask(Activity activity, Corredor corredor){
        this.corredor = corredor;
        this.activity = activity;

    }



    @Override
    protected void onPreExecute() {

        progress = ProgressDialog.show(activity, "Aguarde...","Envio de dados para web", true, true);


        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... params) {


        CorredorJson corredorJson = new CorredorJson();
        data = corredorJson.CorredorToJson(corredor);




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
            editor.putString("perfil", "corredor");
            editor.putString("nome", corredor.getNome());
            editor.putString("email", corredor.getEmail());
            editor.putString("imagemperfil", corredor.getImagemPerfil());
            editor.commit();

            Log.i("preferencias ==== ",prefs.getString("perfil",""));


            ((CadastroUsuarioActivity) activity).onCadastroFinalizado(corredor);
        } else {
            ((CadastroUsuarioActivity) activity).onErro();
        }
    }


}

