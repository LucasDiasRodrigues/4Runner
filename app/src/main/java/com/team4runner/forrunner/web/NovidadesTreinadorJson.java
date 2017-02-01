package com.team4runner.forrunner.web;

import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Lucas on 21/12/2015.
 */
public class NovidadesTreinadorJson {


    public List<Bundle> JsonToTreinosaVencer(String data) {

        List<Bundle> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String nome = (jsonObject.getString("nome"));
                String profPic = (jsonObject.getString("prof_pic"));
                String emailCorredor = (jsonObject.getString("email_corredor"));
                int codTreino = (jsonObject.getInt("cod_treino"));
                String nomeTreino = (jsonObject.getString("nome_treino"));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dtFim = format.parse(jsonObject.getString("dt_fim"));
                int diasRestantes = (jsonObject.getInt("DIAS_RESTANTES"));


                Bundle bundle = new Bundle();
                bundle.putString("nomeCorredor", nome);
                bundle.putString("profPic", profPic);
                bundle.putString("emailCorredor", emailCorredor);
                bundle.putInt("codTreino", codTreino);
                bundle.putString("nomeTreino", nomeTreino);
                bundle.putSerializable("dtFim", dtFim);
                bundle.putInt("diasRestantes", diasRestantes);

                list.add(bundle);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }


    public List<Bundle> JsonToTreinosConcluidos(String data) {

        List<Bundle> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String nome = (jsonObject.getString("nome_corredor"));
                String profPic = (jsonObject.getString("prof_pic"));
                int codTreino = (jsonObject.getInt("cod_treino"));
                String nomeTreino = (jsonObject.getString("nome_treino"));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dtFim = format.parse(jsonObject.getString("dt_fim"));


                Bundle bundle = new Bundle();
                bundle.putString("nomeCorredor", nome);
                bundle.putString("profPic", profPic);
                bundle.putInt("codTreino", codTreino);
                bundle.putString("nomeTreino", nomeTreino);
                bundle.putSerializable("dtFim", dtFim);

                list.add(bundle);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }


    public List<Bundle> JsonToUltimasCorridas(String data) {

        List<Bundle> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String nome = (jsonObject.getString("nome"));
                String profPic = (jsonObject.getString("prof_pic"));
                int codCorrida = (jsonObject.getInt("cod_corrida"));
                String distancia = (jsonObject.getString("distancia"));
                String rit_med = (jsonObject.getString("rit_med"));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dtInicio = format.parse(jsonObject.getString("dt_inicio"));
                DateFormat format2 = new SimpleDateFormat("HH:mm:ss");
                Date duracao = format2.parse(jsonObject.getString("duracao"));

                //Cortar a distancia
                String[] auxDistancia = distancia.split(Pattern.quote("."));


                Bundle bundle = new Bundle();
                bundle.putString("nomeCorredor", nome);
                bundle.putString("profPic", profPic);
                bundle.putInt("codCorrida", codCorrida);
                bundle.putString("distancia", auxDistancia[0]);
                bundle.putString("ritMedio", rit_med);
                bundle.putSerializable("dtInicio", dtInicio);
                bundle.putSerializable("duracao", duracao);

                list.add(bundle);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Bundle> JsonToUltimosTestes(String data) {

        List<Bundle> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String nome = (jsonObject.getString("nome"));
                String profPic = (jsonObject.getString("prof_pic"));
                int codCorrida = (jsonObject.getInt("cod_corrida"));
                String distancia = (jsonObject.getString("distancia"));
                String rit_med = (jsonObject.getString("rit_med"));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dtInicio = format.parse(jsonObject.getString("dt_inicio"));
                DateFormat format2 = new SimpleDateFormat("HH:mm:ss");
                Date duracao = format2.parse(jsonObject.getString("duracao"));

                //Cortar a distancia
                String[] auxDistancia = distancia.split(Pattern.quote("."));

                Bundle bundle = new Bundle();
                bundle.putString("nomeCorredor", nome);
                bundle.putString("profPic", profPic);
                bundle.putInt("codCorrida", codCorrida);
                bundle.putString("distancia", auxDistancia[0]);
                bundle.putString("ritMedio", rit_med);
                bundle.putSerializable("dtInicio", dtInicio);
                bundle.putSerializable("duracao", duracao);

                list.add(bundle);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Bundle> JsonToNovosCorredores(String data) {

        List<Bundle> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String nome = (jsonObject.getString("nome"));
                String profPic = (jsonObject.getString("prof_pic"));
                String emailCorredor = (jsonObject.getString("email"));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dtInicio = format.parse(jsonObject.getString("dt_inicio"));


                Bundle bundle = new Bundle();
                bundle.putString("nomeCorredor", nome);
                bundle.putString("profPic", profPic);
                bundle.putString("emailCorredor", emailCorredor);
                bundle.putSerializable("dtInicio", dtInicio);

                list.add(bundle);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Bundle> JsonToListaAusencias(String data) {

        List<Bundle> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String nome = (jsonObject.getString("nome"));
                String profPic = (jsonObject.getString("prof_pic"));
                int codTreino = (jsonObject.getInt("cod_treino"));
                String emailCorredor = (jsonObject.getString("email"));
                String nomeTreino = (jsonObject.getString("nome_treino"));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dtAusencia = format.parse(jsonObject.getString("data"));


                Bundle bundle = new Bundle();
                bundle.putString("nomeCorredor", nome);
                bundle.putString("profPic", profPic);
                bundle.putString("emailCorredor", emailCorredor);
                bundle.putString("nomeTreino", nomeTreino);
                bundle.putString("codTreino",String.valueOf(codTreino));
                bundle.putSerializable("dtAusencia", dtAusencia);

                list.add(bundle);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Bundle> JsonToListaAusenciasTeste(String data) {

        List<Bundle> list = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String nome = (jsonObject.getString("nome"));
                String profPic = (jsonObject.getString("prof_pic"));
                int codTeste = (jsonObject.getInt("cod_teste"));
                String emailCorredor = (jsonObject.getString("email"));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dtTeste = format.parse(jsonObject.getString("dt_teste"));


                Bundle bundle = new Bundle();
                bundle.putString("nomeCorredor", nome);
                bundle.putString("profPic", profPic);
                bundle.putString("emailCorredor", emailCorredor);
                bundle.putString("codTeste",String.valueOf(codTeste));
                bundle.putSerializable("dtTeste", dtTeste);

                list.add(bundle);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }


}
