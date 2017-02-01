package com.team4runner.forrunner.Postergado_NaoUsado;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Lucas on 01/12/2015.
 */
public class OldRegistroGcmTask extends AsyncTask {

    String regId;
    String metodo;
    Context context;

    private ProgressDialog progress;


    public OldRegistroGcmTask(Context context, String metodo) {
        this.metodo = metodo;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, "Aguarde...", "Processando o Cadastro", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {


        if (metodo.equals("registrar")) {

            regId = OldGCM.getRegistrationId(context);
            if (regId == null) {
                // Faz o registro e pega o registration id
                regId = OldGCM.register(context, "946009923486");
                Log.i("Registrado com sucesso.", regId+" ");
            } else {
                Log.i("ja registrado.", regId);
            }

        }

        return regId;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();
    }
}
