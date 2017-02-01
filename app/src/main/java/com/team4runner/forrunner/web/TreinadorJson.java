package com.team4runner.forrunner.web;

import android.os.Bundle;
import android.util.Log;

import com.team4runner.forrunner.modelo.Treinador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lucas on 13/07/2015.
 */
public class TreinadorJson {

    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    public String TreinadorToJson(Treinador treinador) {

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("nome").value(treinador.getNome())
                    .key("email").value(treinador.getEmail())
                    .key("senha").value(treinador.getSenha())
                    .key("telefone").value(treinador.getTelefone())
                    .key("dataNasc").value(sqlDateFormat.format(treinador.getDataNasc()))
                    .key("localizacao").value(treinador.getLocalizacao())
                    .key("locStatus").value(treinador.getLocStatus())
                    .key("sexo").value(treinador.getSexo())
                    .key("imagemPerfil").value(treinador.getImagemPerfil())
                    .key("cref").value(treinador.getCref())
                    .key("gcmId").value(treinador.getGcmId())
                    .key("formacao").value(treinador.getFormacao())
                    .key("sobreMim").value(treinador.getSobreMim())
                    .key("mediaAvaliacao").value(treinador.getMediaAvaliacao())
                    .key("qtdAvaliacoes").value(treinador.getQtdAvaliacoes())
                    .key("dataCadastro").value(dateFormat.format(treinador.getDataCadastro()))


                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }


    public Treinador JsonToTreinador(String data) {
        Treinador treinador = new Treinador();

        try {
            JSONObject jo = new JSONObject(data);

            treinador.setNome(jo.getString("nome"));
            treinador.setDataNasc(sqlDateFormat.parse(jo.getString("dataNasc")));
            if(!(jo.getString("telefone")).equals("null")){
                treinador.setTelefone(jo.getString("telefone"));
            }else{treinador.setTelefone("");}
            treinador.setEmail(jo.getString("email"));
            treinador.setSexo(jo.getString("sexo"));
            treinador.setImagemPerfil(jo.getString("imagemperfil"));
            treinador.setSenha(jo.getString("senha"));
            treinador.setLocalizacao(jo.getString("localizacao"));
            treinador.setGcmId(jo.getString("gcmId"));
            treinador.setSituacao(jo.getString("situacao"));
            treinador.setCref(jo.getString("cref"));
            treinador.setFormacao(jo.getString("formacao"));

            if (!(jo.getString("sobreMim")).equals("null")) {
                treinador.setSobreMim(jo.getString("sobreMim"));
            } else {
                treinador.setSobreMim("");
            }
            if (!(jo.getString("locStatus")).equals("null")) {
                treinador.setLocStatus(jo.getString("locStatus"));
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            treinador.setDataCadastro(format.parse(jo.getString("dataCadastro")));

            treinador.setQtdAvaliacoes(jo.getInt("qtdAvaliacoes"));
            treinador.setMediaAvaliacao(jo.getDouble("mediaAvaliacao"));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return treinador;
    }

    public List<Treinador> JsonArrayToListaTreinador(String data) {
        List<Treinador> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                Treinador treinador = JsonToTreinador(json.getJSONObject(i).toString());
                list.add(treinador);
                Log.i("Treinador" + i + " ", treinador.getNome());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }


}
