package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.web.CorridaJson;
import com.team4runner.forrunner.web.HttpConnection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lucas on 23/10/2015.
 */
public class ConsultaCorridaTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/consulta_corrida_json.php";
    private  String method;

    private ProgressDialog progress;

    private Context context;
    private String emailCorredor;
    private Date data;
    private String dataSql;
    private String tipo;

    public ConsultaCorridaTask(Context context, String emailCorredor, String data, String tipo){
        this.context = context;
        this.emailCorredor = emailCorredor;
        this.tipo = tipo;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.data = format.parse(data);

            DateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
            dataSql = formatSql.format(this.data);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
      //  progress = ProgressDialog.show(context,"Aguarde...","Obtendo informa��es do servidor",true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        if(tipo.equals("treino")){

            method = "consultaCorridaTreino-json";

        } else if(tipo.equals("teste")){

            method = "consultaCorridaTeste-json";

        } else if(tipo.equals("livre")){

            method = "consultaCorridaLivre-json";

        }

        CorridaJson corridaJson = new CorridaJson();
        String consulta = corridaJson.ConsultaCorridaTreinoToJson(emailCorredor, dataSql);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, consulta);
        Log.i("Script", "ANSWER: " + answer);

        Corrida corrida =corridaJson.JsonToCorrida(answer);


        return corrida;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
   //     progress.dismiss();

        ((VisualizarCorridaActivity)context).setCorrida((Corrida)o);
    }

}
