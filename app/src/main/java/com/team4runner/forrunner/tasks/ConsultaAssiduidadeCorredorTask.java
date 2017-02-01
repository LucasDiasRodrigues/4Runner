package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.team4runner.forrunner.fragment.HistoricoTreinosCorredorFragment;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTreinosFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinoJson;

import java.util.List;

/**
 * Created by Lucas on 27/11/2015.
 */
public class ConsultaAssiduidadeCorredorTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/verifica_assiduidade_por_corredor_json.php";
    private final String method = "lista_assiduidade_treinos-json";
    private final Context context;
    private List<Treino> treinos;
    private Corredor corredor;
    private String data;
    private ProgressDialog progress;
    private String tipo;
    private Fragment fragment;


    public ConsultaAssiduidadeCorredorTask(Context context, List<Treino> treinos, Corredor corredor, String tipo, Fragment fragment){
        this.context = context;
        this.treinos = treinos;
        this.corredor = corredor;
        this.tipo = tipo;
        this.fragment = fragment;
    }


    @Override
    protected Object doInBackground(Object[] params) {

        CorredorJson json = new CorredorJson();
        data = json.CorredorToJson(corredor);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
        Log.i("RetornoAssiduidade", "ANSWER: " + answer);

        TreinoJson treinoJson = new TreinoJson();
        List<Treino> mList = (List<Treino>)treinoJson.JsonAssiduidadeListaTreino(treinos,answer);


        return mList;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(tipo.equals("HistoricoTreinosCorredorFragment")){

            ((HistoricoTreinosCorredorFragment)fragment).AtualizaLista((List<Treino>) o);

        } else if(tipo.equals("MeusCorredoresTreinadorTreinosFragment")){

            ((MeusCorredoresTreinadorTreinosFragment)fragment).AtualizaLista((List<Treino>) o);

        }

    }
}
