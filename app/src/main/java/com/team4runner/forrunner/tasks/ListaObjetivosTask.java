package com.team4runner.forrunner.tasks;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.LocalizarCorredoresActivity;
import com.team4runner.forrunner.NovosCorredoresActivity;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.web.ExercicioJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.ObjetivoJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 05/11/2015.
 */
public class ListaObjetivosTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/lista_objetivos_json.php";
    private final String method = "lista_objetivos-json";
    private Context context;
    private Fragment fragment;
    private String data = "";
    private ProgressDialog progress;


    public ListaObjetivosTask(Context context){
        this.context = context;
    }

    public ListaObjetivosTask(Context context, Fragment fragment){
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Object doInBackground(Object[] params) {


        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
        Log.i("Script", "ANSWER: " + answer);

        ObjetivoJson json = new ObjetivoJson();
        List<Objetivo> listaObj = json.JsonArrayToListaObjetivo(answer);



        return listaObj;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(context instanceof NovosCorredoresActivity){
            ((NovosCorredoresActivity)context).getListObj((List<Objetivo>)o);
        }

        if(context instanceof LocalizarCorredoresActivity){
            ((LocalizarCorredoresActivity)context).getObj((List<Objetivo>)o);
        }

    }
}
