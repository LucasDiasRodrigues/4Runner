package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.LocalizarCorredoresActivity;
import com.team4runner.forrunner.LocalizarTreinadorActivity;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

import java.util.List;

/**
 * Created by Lucas on 07/08/2015.
 */
public class ListaTreinadoresTask extends AsyncTask <Object, Object, List<Treinador>>{
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/lista_treinadores_json.php";
    private final String method = "lista_treinadores-json";
    private final Context context;
    private String data = "";
    private ProgressDialog progress;

    public ListaTreinadoresTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Treinador> doInBackground(Object... params) {

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, this.data);
        Log.i("Script", "ANSWER: " + answer);

        TreinadorJson json = new TreinadorJson();
        List<Treinador> treinadores = json.JsonArrayToListaTreinador(answer);

        return treinadores;
    }

    @Override
    protected void onPostExecute(List<Treinador> treinadors) {
        super.onPostExecute(treinadors);


        if(context instanceof LocalizarTreinadorActivity){
            ((LocalizarTreinadorActivity)context).atualizaListaTreinadores(treinadors);

        }
    }
}
