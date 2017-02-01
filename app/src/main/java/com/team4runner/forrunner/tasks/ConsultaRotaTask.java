package com.team4runner.forrunner.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.modelo.PontoRota;
import com.team4runner.forrunner.web.CorridaJson;
import com.team4runner.forrunner.web.HttpConnection;

import java.util.List;

/**
 * Created by Lucas on 29/12/2015.
 */
public class ConsultaRotaTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/consulta_corrida_json.php";
    private final String method = "consulta_rota-json";
    private Context context;
    private int codCorrida;

    public ConsultaRotaTask(Context context, int codCorrida) {
        this.context = context;
        this.codCorrida = codCorrida;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        CorridaJson corridaJson = new CorridaJson();
        Corrida corrida = new Corrida();
        corrida.setCodCorrida(codCorrida);
        String data = corridaJson.CorridaToJson(corrida);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Script", "ANSWER: " + answer);

        List<PontoRota> pontos = corridaJson.JsonToRota(answer);

        return pontos;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ((VisualizarCorridaActivity)context).setRota((List<PontoRota>)o);
    }


}
