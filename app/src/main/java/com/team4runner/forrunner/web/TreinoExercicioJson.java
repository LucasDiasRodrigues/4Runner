package com.team4runner.forrunner.web;

import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.TreinoExercicio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 17/06/2015.
 */
public class TreinoExercicioJson {



    public TreinoExercicio JsonToTreinoExercicio(String data) {
        TreinoExercicio treinoExercicio = new TreinoExercicio();

        try {
            JSONObject jo = new JSONObject(data);

            treinoExercicio.setSemana(jo.getInt("semana"));
            treinoExercicio.setRitmo(jo.getInt("ritmo"));
            treinoExercicio.setVolume(jo.getInt("volume"));
            treinoExercicio.setOrdem(jo.getInt("ordem"));

            if(!(jo.getString("codCorrida").equals("null"))) {
                treinoExercicio.setCodCorrida(jo.getInt("codCorrida"));
            } else{
                treinoExercicio.setCodCorrida(0);
            }

            Exercicio exercicio = new Exercicio();
            exercicio.setCodExercicio(jo.getInt("codExercicio"));
            treinoExercicio.setExercicio(exercicio);

            if(!(jo.getString("qtdRepeticoes").equals("null"))) {
                treinoExercicio.setQtdRepeticoes(jo.getInt("qtdRepeticoes"));
            }
            if(!(jo.getString("intervaloRepeticoes").equals("null"))) {
                treinoExercicio.setIntervaloRepeticoes(jo.getInt("intervaloRepeticoes"));
            }

            if(!(jo.getString("posIntervalo").equals("null"))) {
                treinoExercicio.setPosIntervalo(jo.getInt("posIntervalo"));
            }

            if(!(jo.getString("data").equals("null"))) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                treinoExercicio.setData(format.parse(jo.getString("data")));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        

        return treinoExercicio;
    }

    public List JsonArrayToListaTreinoExercicio(String data) {
        ArrayList<TreinoExercicio> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                TreinoExercicio treinoExercicio = JsonToTreinoExercicio(json.getJSONObject(i).toString());
                list.add(treinoExercicio);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }


}
