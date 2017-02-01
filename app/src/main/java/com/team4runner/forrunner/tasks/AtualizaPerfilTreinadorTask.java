package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

import java.io.ByteArrayOutputStream;

/**
 * Created by Lucas on 21/08/2015.
 */
public class AtualizaPerfilTreinadorTask extends AsyncTask<Object,Object,Object> {
    private Activity activity;
    private Treinador treinador;
    private Bitmap imagemperfil;
    String imagemDecodificada = "";
    Bitmap imagemfotoReduzida;


    private ProgressDialog progress;

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/atualiza_treinador_json.php";
    private final String method = "atualizaTreinador-json";


    public AtualizaPerfilTreinadorTask(Activity activity, Treinador treinador, Bitmap imagemperfil){
        this.activity = activity;
        this.treinador = treinador;
        this.imagemperfil = imagemperfil;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...","Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object... params) {
        //Tratar ImageViwew
        //localiza e transforma em um array de bytes
        if (imagemperfil != null) {
            ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
            imagemperfil.compress(Bitmap.CompressFormat.JPEG, 50, bAOS);
            byte[] imagemArrayBytes = bAOS.toByteArray();

            //decodifica com a classe BASE64 e transforma em string
            imagemDecodificada = Base64.encodeToString(imagemArrayBytes, Base64.DEFAULT);
            treinador.setImagemPerfil(imagemDecodificada);
        }

        TreinadorJson json = new TreinadorJson();
        String jsonTreinador = json.TreinadorToJson(treinador);
        Log.i("==SuperRequisicao =====", jsonTreinador);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonTreinador);

        Log.i("==SuperResposta =====",answer);


        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        if(((String)o).equals("sucesso")){
            Toast.makeText(activity, R.string.alteracaoefetuada, Toast.LENGTH_SHORT).show();
        }

    }
}
