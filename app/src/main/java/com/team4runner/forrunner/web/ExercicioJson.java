package com.team4runner.forrunner.web;

import android.util.Log;

import com.team4runner.forrunner.modelo.Exercicio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 30/06/2015.
 */
public class ExercicioJson {

    public String ExercicioToJson(Exercicio exercicio) {
        // Log.i("exercicio json", exercicio.toString());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("codExercicio").value(exercicio.getCodExercicio())
                    .key("nomeExercicio").value(exercicio.getNomeExercicio())
                    .key("descricaoExercicio").value(exercicio.getDescricaoExercicio())
                    .key("emailTreinador").value(exercicio.getEmailTreinador())
                    .key("dataCadastro").value(dateFormat.format(exercicio.getDataCadastro()))
                    .key("situacao").value(exercicio.getSituacao())

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }


    public Exercicio JsonToExercicio(String data) {
        Exercicio exercicio = new Exercicio();

        try {
            JSONObject jo = new JSONObject(data);


            exercicio.setCodExercicio(jo.getInt("codExercicio"));
            exercicio.setNomeExercicio(jo.getString("nomeExercicio"));
            if(!(jo.getString("descricaoExercicio").equals("null"))){
                exercicio.setDescricaoExercicio(jo.getString("descricaoExercicio"));
            }
            exercicio.setEmailTreinador(jo.getString("emailTreinador"));
            exercicio.setSituacao(jo.getString("situacao"));

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            exercicio.setDataCadastro(format.parse(jo.getString("dataCadastro")));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return exercicio;
    }

    public List JsonArrayToListaExercicio(String data) {
        List<Exercicio> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                Exercicio exercicio = JsonToExercicio(json.getJSONObject(i).toString());
                list.add(exercicio);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }


}
