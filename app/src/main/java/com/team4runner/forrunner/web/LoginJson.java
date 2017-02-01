package com.team4runner.forrunner.web;

import android.util.Log;

import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucas on 03/06/2015.
 */
public class LoginJson {

    private String resposta = "";

    public String loginToJson(String email, String senha, String gcmId) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("email", email);
            jo.put("senha", senha);
            jo.put("gcmId", gcmId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    public Object loginToString(String data) {

        Corredor corredor = new Corredor();
        Treinador treinador = new Treinador();
        try {
            JSONObject jo = new JSONObject(data);

            //Se for corredor
            if (jo.getString("perfil").equals("corredor")) {

                corredor.setEmail(jo.getString("email"));
                corredor.setNome(jo.getString("nome"));
                corredor.setImagemPerfil(jo.getString("imagemperfil"));

                jo.put("nome", corredor.getNome());
                jo.put("email", corredor.getEmail());
                jo.put("imagemperfil", corredor.getImagemPerfil());

                // APRESENTA??O
                Log.i("Script", "nome: " + corredor.getNome());
                Log.i("Script", "email: " + corredor.getEmail());
                Log.i("Script", "imagem do perfil: " + corredor.getImagemPerfil());

                resposta = "corredor";


            } else  {  //Se for instrutor

                treinador.setEmail(jo.getString("email"));
                treinador.setNome(jo.getString("nome"));
                treinador.setImagemPerfil(jo.getString("imagemperfil"));

                jo.put("nome", treinador.getNome());
                jo.put("email", treinador.getEmail());
                jo.put("imagemperfil", treinador.getImagemPerfil());

                // APRESENTA??O
                Log.i("Script", "nome: " + treinador.getNome());
                Log.i("Script", "email: " + treinador.getEmail());
                Log.i("Script", "imagem do perfil: " + treinador.getImagemPerfil());

                resposta = "treinador";


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(resposta.equals("corredor")){
            return corredor;
        }else  if(resposta.equals("treinador")){
            return treinador;
        } else {
            return "";
        }

    }

    public String logoffToJson(String email,String origem) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("email", email);
            jo.put("origem", origem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }


}
