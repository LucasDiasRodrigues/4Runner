package com.team4runner.forrunner.web;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.PontoRota;

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
 * Created by Lucas on 15/10/2015.
 */
public class CorridaJson {

    public String CorridaToJson(Corrida corrida) {


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");


        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("codCorrida").value(corrida.getCodCorrida())
                    .key("dtHoraInicio").value(dateFormat.format(corrida.getDtHoraInicio()))
                    .key("dtHoraFim").value(dateFormat.format(corrida.getDtHoraFim()))
                    .key("velocidadeMedia").value(corrida.getVelocidadeMedia())
                    .key("velocidadeMax").value(corrida.getVelocidadeMax())
                    .key("temperatura").value(corrida.getTemperatura())
                    .key("pSE").value(corrida.getpSE())
                    .key("obs").value(corrida.getObs())
                    .key("distancia").value(corrida.getDistancia())
                    .key("clima").value(corrida.getClima())
                    .key("terreno").value(corrida.getTerreno())
                    .key("calorias").value(corrida.getCalorias())
                    .key("ritmoMedio").value(corrida.getRitmoMedio())
                    .key("tipo").value(corrida.getTipo())
                    .key("emailCorredor").value(corrida.getEmailCorredor())
                    .key("rota").array();

            ArrayList<PontoRota> rota = corrida.getRota();

            for (PontoRota ponto : rota) {

                jsonStringer.object()
                        .key("lat").value(ponto.getLatLng().latitude)
                        .key("lng").value(ponto.getLatLng().longitude)
                        .key("velocidade").value(ponto.getVelocidade())
                        .key("distancia").value(ponto.getDistancia())
                        .key("tempo").value(ponto.getTempo())
                        .key("data").value(dateFormat.format(ponto.getDtHora()))

                        .endObject();
            }

            jsonStringer.endArray().endObject();


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonStringer.toString();
    }


    public Corrida JsonToCorrida(String data) {

        Corrida corrida = new Corrida();
        try {
            JSONObject json = new JSONObject(data);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            corrida.setCodCorrida(json.getInt("cod_corrida"));
            corrida.setEmailCorredor(json.getString("email_corredor"));
            corrida.setDtHoraInicio(format.parse(json.getString("dt_inicio")));
            corrida.setDtHoraFim(format.parse(json.getString("dt_fim")));
            corrida.setVelocidadeMax(Float.parseFloat(json.getString("vel_max")));
            corrida.setVelocidadeMedia(Float.parseFloat(json.getString("vel_med")));
            corrida.setTemperatura(json.getInt("temperatura"));
            corrida.setDistancia(Float.parseFloat(json.getString("distancia")));
            corrida.setpSE(json.getInt("pse"));
            corrida.setClima(json.getString("clima"));
            corrida.setTerreno(json.getString("terreno"));
            corrida.setObs(json.getString("obs"));
            corrida.setCalorias(Float.parseFloat(json.getString("kcal")));
            corrida.setRitmoMedio(Float.parseFloat(json.getString("rit_med")));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return corrida;


    }


    public List<Corrida> JsonArrayToListaCorrida(String data) {
        List<Corrida> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                Corrida corrida = JsonToCorrida(json.getJSONObject(i).toString());
                list.add(corrida);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }


    public ArrayList<PontoRota> JsonToRota(String data) {


        ArrayList<PontoRota> pontos = new ArrayList<PontoRota>();
        LatLng auxLatLng;

        try {
            JSONArray json = new JSONArray(data);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            for (int i = 0; i < json.length(); i++) {

                PontoRota pontoRota = new PontoRota();

                auxLatLng = new LatLng(json.getJSONObject(i).getDouble("lat"), json.getJSONObject(i).getDouble("lng"));
                pontoRota.setLatLng(auxLatLng);

                pontoRota.setVelocidade(Float.parseFloat(json.getJSONObject(i).getString("velocidade")));
                pontoRota.setDistancia(Float.parseFloat(json.getJSONObject(i).getString("distancia")));
                pontoRota.setTempo(Float.parseFloat(json.getJSONObject(i).getString("tempo")));
                pontoRota.setDtHora(format.parse(json.getJSONObject(i).getString("dt_hr")));

                pontos.add(pontoRota);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return pontos;


    }


    public String ConsultaCorridaTreinoToJson(String emailCorredor, String data) {

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("corredor").value(emailCorredor)
                    .key("data").value(data)

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }


}
