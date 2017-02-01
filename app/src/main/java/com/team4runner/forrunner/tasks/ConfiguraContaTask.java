package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.ConfiguracaoContaActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

/**
 * Created by Lucas on 19/01/2016.
 */
public class ConfiguraContaTask extends AsyncTask <Object, String, String> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/configura_conta_json.php";
    private String method;
    private String data;

    private Context context;
    private Treinador treinador;
    private Corredor corredor;
    private String perfil;

    private ProgressDialog progress;

    public ConfiguraContaTask(Context context, Corredor corredor, String metodo){
        this.context = context;
        this.corredor = corredor;
        perfil = "corredor";

        switch (metodo){
            case "alterarSenha":
                method = "altera_senha_corredor-json";
                break;
            case "alterarLocStatus":
                method = "altera_locstatus_corredor-json";
                break;
        }
    }
    public ConfiguraContaTask(Context context, Treinador treinador, String metodo){
        this.context = context;
        this.treinador = treinador;
        perfil = "treinador";

        switch (metodo){
            case "alterarSenha":
                method = "altera_senha_treinador-json";
                break;
            case "alterarLocStatus":
                method = "altera_locstatus_treinador-json";
                break;
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);

    }


    @Override
    protected String doInBackground(Object... params) {

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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progress.dismiss();

        switch (s){
            case "senhasucesso":
                Toast.makeText(context, context.getText(R.string.senhaalterada),Toast.LENGTH_SHORT).show();
                ((ConfiguracaoContaActivity)context).limparCampos();
                break;
            case "senhaincorreta":
                Toast.makeText(context, context.getText(R.string.senhaincorreta),Toast.LENGTH_SHORT).show();
                ((ConfiguracaoContaActivity)context).senhaincorreta();
                break;
            case "locstatussucesso":

                SharedPreferences prefs = context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if(perfil.equals("corredor")){
                    editor.putString("locStatus", corredor.getLocStatus());
                } else if(perfil.equals("treinador")){
                    editor.putString("locStatus", treinador.getLocStatus());
                }
                editor.commit();


                break;
            default:
                Toast.makeText(context, context.getText(R.string.opserro),Toast.LENGTH_SHORT).show();
                break;
        }



    }
}
