package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroUsuarioActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

import java.io.ByteArrayOutputStream;


/**
 * Created by Lucas on 28/06/2015.
 */
public class CadastroTreinadorBasicoTask extends AsyncTask<Object,Object,Object> {


    private Activity activity;
    private Treinador treinador;
    private Bitmap imagemperfil;
    private ProgressDialog progress;
    Bundle treinadorBundle;
    String imagemDecodificada = "";
    Bitmap imagemfotoReduzida;

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_treinador_json.php";
    private final String method = "cadastroBasicoTreinador-json";


    public CadastroTreinadorBasicoTask(Activity activity, Treinador treinador,Bitmap imagemperfil){
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
        } else {
            treinador.setImagemPerfil("");
        }

        TreinadorJson json = new TreinadorJson();
        String jsonTreinador = json.TreinadorToJson(treinador);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonTreinador);

        treinadorBundle = new Bundle();
        treinadorBundle.putSerializable("treinador", treinador);


        return answer;
        
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();



        Log.i("resultado Json", o.toString());

        if (o.toString().equals("Sucesso")) {
            this.treinadorBundle.putSerializable("corredor", this.treinador);


            ((CadastroUsuarioActivity) activity).onCadastroComplementarTreinador(treinadorBundle);
        } else if (o.toString().equals("EmailJaCadastrado")) {

            new AlertDialog.Builder(activity).setTitle(R.string.atencao)
                    .setMessage(R.string.emailjacadastrado)
                    .setPositiveButton(R.string.okentendi, null)
                    .show();
        } else {
            ((CadastroUsuarioActivity) activity).onErro();
        }


    }
}
