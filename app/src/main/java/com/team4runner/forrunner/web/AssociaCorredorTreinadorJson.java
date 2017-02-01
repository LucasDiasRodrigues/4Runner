package com.team4runner.forrunner.web;

import android.util.Log;

import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Created by Lucas on 12/08/2015.
 */
public class AssociaCorredorTreinadorJson {

    public String associaCorredorTreinador(Corredor corredor, Treinador treinador, String substitui){

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("emailCorredor").value(corredor.getEmail())
                    .key("emailTreinador").value(treinador.getEmail())
                    .key("substuiSolicitacao").value(substitui)

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }

}
