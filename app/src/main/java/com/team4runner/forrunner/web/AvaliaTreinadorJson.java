package com.team4runner.forrunner.web;

import android.util.Log;

import com.team4runner.forrunner.modelo.Corredor;
import org.json.JSONException;
import org.json.JSONStringer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Lucas on 22/03/2016.
 */


public class AvaliaTreinadorJson {


    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date dataHoje = new Date();

    public String avaliacaoToJson(String emailCorredor, String emailTreinador, int voto) {
        // Log.i("corredor json", corredor.toString());


        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("emailCorredor").value(emailCorredor)
                    .key("emailTreinador").value(emailTreinador)
                    .key("voto").value(voto)
                    .key("dataVoto").value(sqlDateFormat.format(dataHoje))

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }



}
