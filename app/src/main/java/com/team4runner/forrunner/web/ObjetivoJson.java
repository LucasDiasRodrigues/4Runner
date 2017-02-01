package com.team4runner.forrunner.web;

import com.team4runner.forrunner.modelo.Objetivo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 05/11/2015.
 */
public class ObjetivoJson {


    private String resposta;

    public Objetivo JsonToObj(String data) {

        Objetivo objetivo = new Objetivo();
        try {
            JSONObject jo = new JSONObject(data);


            objetivo.setCodObj(jo.getInt("codObj"));
            objetivo.setDescricao(jo.getString("descricao"));


            return objetivo;

        } catch (JSONException e1) {
            e1.printStackTrace();
            return objetivo;
        }

    }

    public List JsonArrayToListaObjetivo(String data) {
        List<Objetivo> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                Objetivo objetivo = JsonToObj(json.getJSONObject(i).toString());
                list.add(objetivo);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }

}
