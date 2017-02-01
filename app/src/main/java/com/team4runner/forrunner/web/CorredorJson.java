package com.team4runner.forrunner.web;

import android.os.Bundle;
import android.util.Log;

import com.team4runner.forrunner.modelo.Corredor;
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
 * Created by Lucas on 17/06/2015.
 */
public class CorredorJson {

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat sqlDateHrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String CorredorToJson(Corredor corredor) {
        // Log.i("corredor json", corredor.toString());



        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("nome").value(corredor.getNome())
                    .key("email").value(corredor.getEmail())
                    .key("senha").value(corredor.getSenha())
                    .key("telefone").value(corredor.getTelefone())
                    .key("dataNasc").value(sqlDateFormat.format(corredor.getDataNasc()))
                    .key("localizacao").value(corredor.getLocalizacao())
                    .key("sexo").value(corredor.getSexo())
                    .key("imagemPerfil").value(corredor.getImagemPerfil())
                    .key("peso").value(corredor.getPeso())
                    .key("altura").value(corredor.getAltura())
                    .key("objetivo").value(corredor.getObjetivo())
                    .key("emailTreinador").value(corredor.getEmailTreinador())
                    .key("situacao").value(corredor.getSituacao())
                    .key("sobreMim").value(corredor.getSobreMim())
                    .key("locStatus").value(corredor.getLocStatus())
                    .key("gcmId").value(corredor.getGcmId())
                    .key("dataCadastro").value(dateFormat.format(corredor.getDataCadastro()))

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }


    public Corredor JsonToCorredor(String data) {
        Corredor corredor = new Corredor();

        try {
            JSONObject jo = new JSONObject(data);

            corredor.setNome(jo.getString("nome"));
            corredor.setDataNasc(sqlDateFormat.parse(jo.getString("dataNasc")));
            if (!(jo.getString("telefone")).equals("null")) {
                corredor.setTelefone(jo.getString("telefone"));
            } else {
                corredor.setTelefone("");
            }
            corredor.setEmail(jo.getString("email"));
            corredor.setSexo(jo.getString("sexo"));
            if (!(jo.getString("sobreMim")).equals("null")) {
                corredor.setSobreMim(jo.getString("sobreMim"));
            } else {
                corredor.setSobreMim("");
            }
            corredor.setImagemPerfil(jo.getString("imagemperfil"));
            corredor.setSenha(jo.getString("senha"));
            corredor.setLocalizacao(jo.getString("localizacao"));
            corredor.setGcmId(jo.getString("gcmId"));
            corredor.setSituacao(jo.getString("situacao"));
            if (!(jo.getString("peso")).equals("null")) {
                corredor.setPeso(jo.getDouble("peso"));
            } else {
                corredor.setPeso(1.0);
            }
            corredor.setEmailTreinador(jo.getString("emailTreinador"));
            if (!(jo.getString("altura")).equals("null")) {
                corredor.setAltura(Double.parseDouble(jo.getString("altura")));
            } else {
                corredor.setAltura(1.0);
            }

            if (!(jo.getString("locStatus")).equals("null")) {
                corredor.setLocStatus(jo.getString("locStatus"));
            }
            if (!(jo.getString("objetivo")).equals("null")) {
                corredor.setObjetivo(jo.getInt("objetivo"));
            }
            if (!(jo.getString("dataCadastro")).equals("null")) {
                corredor.setDataCadastro(sqlDateHrFormat.parse(jo.getString("dataCadastro")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //teste
        double aux = 123;
        Log.i("Teste", aux + "");
        Log.i("Peso", corredor.getPeso() + "");
        Log.i("Altura", corredor.getAltura() + "");


        return corredor;
    }

    public List JsonArrayToListaCorredor(String data) {
        List<Corredor> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                Corredor corredor = JsonToCorredor(json.getJSONObject(i).toString());
                list.add(corredor);
                Log.i("Corredor" + i + " ", corredor.getNome());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }

    public String ConsutaTreinadorToJson(String emailCorredor, String emailTreinador) {

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("emailCorredor").value(emailCorredor)
                    .key("emailTreinador").value(emailTreinador)

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();

    }

    public Bundle JsonConsutaTreinadorToBundle(String data) {

        TreinadorJson treinadorJson = new TreinadorJson();
        Treinador treinador = new Treinador();
        int ultimoVotoCorredor = 0;
        Date dataUltimoVotoCorredor = null;
        Bundle bundle = new Bundle();

        try {
            JSONObject jo = new JSONObject(data);

            treinador = treinadorJson.JsonToTreinador(data);

            if (!(jo.getString("ultimoVotoCorredor")).equals("null")) {
                ultimoVotoCorredor = jo.getInt("ultimoVotoCorredor");
            }
            if (!(jo.getString("dataUltimoVotoCorredor")).equals("null")) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                dataUltimoVotoCorredor = format.parse(jo.getString("dataUltimoVotoCorredor"));
            }

            bundle.putSerializable("treinador", treinador);
            bundle.putInt("ultimoVotoCorredor", ultimoVotoCorredor);
            bundle.putSerializable("dataUltimoVotoCorredor", dataUltimoVotoCorredor);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return bundle;
    }
}
