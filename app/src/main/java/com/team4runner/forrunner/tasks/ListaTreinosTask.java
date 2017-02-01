package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.team4runner.forrunner.fragment.EstatisticasCorredorFragment;
import com.team4runner.forrunner.fragment.HistoricoTreinosCorredorFragment;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTreinosFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinoJson;

import java.util.List;

/**
 * Created by Lucas on 27/08/2015.
 */
public class ListaTreinosTask extends AsyncTask<Object, Object, Object> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/lista_treino_json.php";
    private String method = "listaTreinoPorCorredor-json";

    private ProgressDialog progress;

    private Activity activity;
    private Corredor corredor;
    private List<Treino> treinos;
    private String tipo;

    private Fragment fragment;

    public ListaTreinosTask(Activity activity, Corredor corredor, String tipo, Fragment fragment, String perfil){
        this.activity = activity;
        this.corredor = corredor;
        this.tipo = tipo;
        this.fragment = fragment;

        if(perfil.equals("corredor")){
            method = "listaTreinoPorCorredor-json";
        } else if(perfil.equals("treinador")){
            method = "listaTreinoPorTreinador-json";
        }
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... params) {

        CorredorJson json = new CorredorJson();
        String jsonCorredor = json.CorredorToJson(corredor);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonCorredor);
        Log.i("ResultadoTreino =", answer);

        TreinoJson treinoJson = new TreinoJson();
        treinos = treinoJson.JsonArrayToListaTreino(answer);


        return treinos;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(tipo.equals("HistoricoTreinosCorredorFragment")){

            ((HistoricoTreinosCorredorFragment)fragment).VerificaAssiduidade(treinos);
            Log.i("Assiduidade", "inicio verificacao ");

        } else if(tipo.equals("EstatisticasCorredorFragment")){

            ((EstatisticasCorredorFragment)fragment).preencherSpinnerTreinos(treinos);

        }
        else if(tipo.equals("MeusCorredoresTreinadorTreinosFragment")){

            ((MeusCorredoresTreinadorTreinosFragment)fragment).VerificaAssiduidade(treinos);

        }

    }
}
