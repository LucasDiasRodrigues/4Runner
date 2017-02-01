package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.team4runner.forrunner.fragment.HistoricoLivreCorredorFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.CorridaJson;
import com.team4runner.forrunner.web.HttpConnection;

import java.util.List;

/**
 * Created by Lucas on 28/01/2016.
 */
public class ListaCorridasTask extends AsyncTask<Object, Object, Object> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/consulta_corrida_json.php";
    private String method;

    private Corredor corredor;
    private Activity activity;
    private Fragment fragment;


    public ListaCorridasTask(Activity activity, Corredor corredor, String tipoCorrida, Fragment fragment) {
        this.corredor = corredor;
        this.activity = activity;
        this.fragment = fragment;
        if(tipoCorrida.equals("livre")){
            method = "lista_corrida_livre-json";
        } else if(tipoCorrida.equals("teste")){
            method = "lista_corrida_teste-json";
        }

    }

    @Override
    protected Object doInBackground(Object... params) {

        CorredorJson corredorJson = new CorredorJson();
        String data = corredorJson.CorredorToJson(corredor);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Script", "ANSWER: " + answer);

        CorridaJson corridaJson = new CorridaJson();
        List<Corrida> mList = corridaJson.JsonArrayToListaCorrida(answer);


        return mList;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(fragment != null && fragment instanceof HistoricoLivreCorredorFragment){

            ((HistoricoLivreCorredorFragment)fragment).atualizaListaCorridas((List<Corrida>)o);

        }

    }
}
