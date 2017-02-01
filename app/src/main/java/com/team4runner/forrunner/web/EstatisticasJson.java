package com.team4runner.forrunner.web;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lucas on 05/01/2016.
 */
public class EstatisticasJson {


    public Bundle JsonToEstatisticas(String data) {


        Bundle bundle = new Bundle();

        try {
            JSONObject jsonObject = new JSONObject(data);

            Double distancia = (jsonObject.getDouble("TOTAL_DISTANCIA"));
            String duracao = (jsonObject.getString("TOTAL_DURACAO"));
            Double velocidadeMax = (jsonObject.getDouble("TOTAL_VEL_MAX"));
            Double calorias = (jsonObject.getDouble("TOTAL_KCAL"));

            bundle.putDouble("distancia", distancia);
            bundle.putString("duracao", duracao);
            bundle.putDouble("velocidadeMax", velocidadeMax);
            bundle.putDouble("calorias", calorias);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bundle;
    }

}
