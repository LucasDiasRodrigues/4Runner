package com.team4runner.forrunner.web;

import android.util.Log;

import com.team4runner.forrunner.modelo.TesteDeCampo;

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
 * Created by Lucas on 06/01/2016.
 */
public class TesteDeCampoJson {

    DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public TesteDeCampo JsonToTesteDeCampo(String json) {

        TesteDeCampo testeDeCampo = new TesteDeCampo();

        try {
            JSONObject jo = new JSONObject(json);

            testeDeCampo.setCodTeste(jo.getInt("cod_teste"));
            testeDeCampo.setVolume(jo.getDouble("volume"));
            testeDeCampo.setTipoVolume(jo.getString("tp_volume"));
            testeDeCampo.setDataCadastro(dateTimeFormat.parse(jo.getString("dt_cadastro")));
            testeDeCampo.setEmailALuno(jo.getString("email_corredor"));
            testeDeCampo.setEmailTreinador(jo.getString("email_treinador"));
            testeDeCampo.setDataTeste(dateFormat.parse(jo.getString("dt_teste")));
            testeDeCampo.setSituacao(jo.getString("situacao"));
            if (!(jo.getString("obs").equals("null"))) {
                testeDeCampo.setObs(jo.getString("obs"));
            }

            if (!(jo.getString("cod_corrida").equals("null"))) {
                testeDeCampo.setCodCorrida(jo.getInt("cod_corrida"));
            }




        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return testeDeCampo;


    }

    public List<TesteDeCampo> ArrayJsonToListTesteDeCampo(String arrayJson) {

        List<TesteDeCampo> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(arrayJson);

            for (int i = 0; i < json.length(); i++) {
                TesteDeCampo testeDeCampo = JsonToTesteDeCampo(json.getJSONObject(i).toString());
                list.add(testeDeCampo);
                Log.i("Teste" + i + " ", testeDeCampo.getEmailALuno());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }

    public String TesteDeCampoToJson(TesteDeCampo testeDeCampo){

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("cod_teste").value(testeDeCampo.getCodTeste())
                    .key("volume").value(testeDeCampo.getVolume())
                    .key("tp_volume").value(testeDeCampo.getTipoVolume())
                    .key("dt_cadastro").value(dateTimeFormat.format(testeDeCampo.getDataCadastro()))
                    .key("email_corredor").value(testeDeCampo.getEmailALuno())
                    .key("email_treinador").value(testeDeCampo.getEmailTreinador())
                    .key("dt_teste").value(dateFormat.format(testeDeCampo.getDataTeste()))
                    .key("situacao").value(testeDeCampo.getSituacao())
                    .key("obs").value(testeDeCampo.getObs())
                    .key("cod_corrida").value(testeDeCampo.getCodCorrida())

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());

        return jsonStringer.toString();
    }


}
