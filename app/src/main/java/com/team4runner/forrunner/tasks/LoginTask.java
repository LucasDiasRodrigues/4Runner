package com.team4runner.forrunner.tasks;

import com.team4runner.forrunner.MainActivity;
import com.team4runner.forrunner.MainCorredorActivity;
import com.team4runner.forrunner.MainTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.LoginJson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Lucas on 03/06/2015.
 */
public class LoginTask extends AsyncTask<Object, Object, Object> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/login.php";
    private final String method = "login-json";
    private final MainActivity activity;
    private String data;
    private ProgressDialog progress;

    public LoginTask(String data, MainActivity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object... params) {

        if (HttpConnection.isNetworkAvailable(activity)) {
            Log.i("CONEXAO", "CONECTADO");

            String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);

            Log.i("Script", "ANSWER: " + answer);

            //Se a senha estiver incorreta
            if (answer.equals("\"senhaincorreta\"")) {
                String resposta = "senhaIncorreta";
                return resposta;

            } else if (answer.equals("\"naolocalizado\"")) {

                String resposta2 = "naoLocalizado";
                return resposta2;

            } else if (!answer.equals("")) {
                Object resposta = new LoginJson().loginToString(answer);

                if (resposta instanceof Corredor) {

                    Corredor corredorLogado;
                    corredorLogado = (Corredor) resposta;

                    SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("logado", true);
                    editor.putString("perfil", "corredor");
                    editor.putString("nome", corredorLogado.getNome());
                    editor.putString("email", corredorLogado.getEmail());
                    editor.putString("imagemperfil", corredorLogado.getImagemPerfil());
                    editor.commit();

                    Log.i("preferencias ==== ", prefs.getString("perfil", ""));

                    return corredorLogado;


                } else if (resposta instanceof Treinador){
                    Treinador treinadorLogado;
                    treinadorLogado = (Treinador) resposta;

                    SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("logado", true);
                    editor.putString("perfil", "treinador");
                    editor.putString("nome", treinadorLogado.getNome());
                    editor.putString("email", treinadorLogado.getEmail());
                    editor.putString("imagemperfil", treinadorLogado.getImagemPerfil());
                    editor.commit();

                    Log.i("preferencias ==== ", prefs.getString("perfil", ""));


                    return treinadorLogado;

                } else{
                    Log.i("CONECXAO", "DESCONECTADO");
                    return "erroConexao";
                }


            } else {
                Log.i("CONECXAO", "DESCONECTADO");
                return "erroConexao";
            }

        } else {
            Log.i("CONECXAO", "DESCONECTADO");
            return "erroConexao";
        }

    }

    @Override
    protected void onPostExecute(Object resultado) {
        super.onPostExecute(resultado);
        progress.dismiss();

        if (resultado.equals("erroConexao")) {
            Toast.makeText(activity, R.string.conexaoIndosponivel, Toast.LENGTH_LONG).show();

        } else if (resultado.equals("senhaIncorreta") || resultado.equals("naoLocalizado")) {
            Toast.makeText(activity, R.string.dadosInvalidos, Toast.LENGTH_SHORT).show();

        } else if (resultado instanceof Corredor) {
            Intent intent = new Intent(activity, MainCorredorActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else if (resultado instanceof Treinador) {
            Intent intent = new Intent(activity, MainTreinadorActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }


    }
}
