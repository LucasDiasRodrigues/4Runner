package com.team4runner.forrunner.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.team4runner.forrunner.fragment.EstatisticasCorredorFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.EstatisticasJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinoJson;

/**
 * Created by Lucas on 05/01/2016.
 */
public class EstatisticasTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/estatisticas_json.php";
    private String method;

    private Context context;
    private Corredor corredor;
    private Treino treino;
    private String tipo;

    String answer;
    String data;
    CorredorJson corredorJson = new CorredorJson();
    TreinoJson treinoJson = new TreinoJson();
    EstatisticasJson estJson = new EstatisticasJson();

    Bundle bundle = new Bundle();

    private Fragment fragment;


    public EstatisticasTask (Context context,String tipo, Corredor corredor, Fragment fragment){
        this.context = context;
        this.corredor = corredor;
        this.tipo = tipo;
        this.fragment = fragment;
    }

    public EstatisticasTask (Context context,String tipo, Treino treino, Fragment fragment){
        this.context = context;
        this.treino = treino;
        this.tipo = tipo;
        this.fragment = fragment;
    }


    @Override
    protected Bundle doInBackground(Object[] params) {

        switch (tipo){
            case "geral":
                method = "estatisticas_geral-json";
                data = corredorJson.CorredorToJson(corredor);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task", " " + answer);

                bundle = estJson.JsonToEstatisticas(answer);

                break;
            case "porTreino":
                method = "estatisticas_treino-json";

                data = treinoJson.TreinoToJson(treino);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task", " " + answer);

                bundle = estJson.JsonToEstatisticas(answer);

                break;
            case "corridasLivres":
                method = "estatisticas_livre-json";
                data = corredorJson.CorredorToJson(corredor);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task", " " + answer);

                bundle = estJson.JsonToEstatisticas(answer);

                break;
        }


        return bundle;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(fragment != null && fragment instanceof EstatisticasCorredorFragment){

            switch (tipo){
                case "geral":

                    ((EstatisticasCorredorFragment)fragment).preencheEtatisticasGerais((Bundle)o);



                    break;
                case "porTreino":

                    ((EstatisticasCorredorFragment)fragment).preencheEstatisticasPorTreino((Bundle)o);


                    break;
                case "corridasLivres":
                    ((EstatisticasCorredorFragment)fragment).preencheEstatisticasPorCorridasLivres((Bundle)o);

                    break;
            }




        }

    }
}
