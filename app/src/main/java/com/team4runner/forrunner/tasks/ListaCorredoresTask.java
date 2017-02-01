package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.team4runner.forrunner.LocalizarCorredoresActivity;
import com.team4runner.forrunner.fragment.HomeTreinadorMeusCorredoresFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

import java.util.List;

/**
 * Created by Lucas on 23/07/2015.
 */
public class ListaCorredoresTask extends AsyncTask<Object, Object, List<Corredor>> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/lista_corredores_json.php";
    private String method;
    private final Context context;
    private String data;
    private ProgressDialog progress;
    private Treinador treinador;
    private boolean comTreinador;
    private Fragment fragment;


    public ListaCorredoresTask(Treinador treinador, Context context, String metodo, Fragment fragment) {
        this.treinador = treinador;
        this.context = context;
        this.fragment = fragment;

        if(metodo.equals("meusCorredores")){
            method = "lista_corredores_ativos_por_treinador-json";

        } else if(metodo.equals("procurarCorredores")){
            method = "treinador_localiza_corredores-json";
        }

        comTreinador = true;
    }

    public ListaCorredoresTask(Context context) {
        this.context = context;
        method = "lista_corredores_ativos-json";
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List doInBackground(Object... params) {

        if(comTreinador){
            TreinadorJson treinadorJson = new TreinadorJson();
            data = treinadorJson.TreinadorToJson(treinador);
        } else {
            data = "";
        }


        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
        Log.i("Script", "ANSWER: " + answer);

        CorredorJson json = new CorredorJson();
        List<Corredor> corredores = json.JsonArrayToListaCorredor(answer);


        return corredores;
    }

    @Override
    protected void onPostExecute(List<Corredor> o) {
        super.onPostExecute(o);

        if(fragment != null){

            if(fragment instanceof HomeTreinadorMeusCorredoresFragment){
                ((HomeTreinadorMeusCorredoresFragment)fragment).atualizaListaCorredores(o);

            }


        }

        if(context instanceof LocalizarCorredoresActivity){
            ((LocalizarCorredoresActivity)context).atualizaListaCorredores(o);

        }




    }
}
