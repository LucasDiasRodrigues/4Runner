package com.team4runner.forrunner.web;

import android.util.Log;

import com.team4runner.forrunner.modelo.PontoRota;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;

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
 * Created by Lucas on 17/06/2015.
 */
public class TreinoJson {

    public String TreinoToJson(Treino treino) {
        // Log.i("treino json", treino.toString());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("codTreino").value(treino.getCodTreino())
                    .key("nome").value(treino.getNome())
                    .key("qtdSemanas").value(treino.getQtdSemanas())
                    .key("qtdDias").value(treino.getQtdSemanas())
                    .key("obs").value(treino.getObservacao())
                    .key("obj").value(treino.getObjetivo())
                    .key("dtInicio").value(sqlDateFormat.format(treino.getDtInicio()))
                    .key("dtFim").value(sqlDateFormat.format(treino.getDtFim()))
                    .key("avaliacao").value(treino.getAvaliacaoDoCorredor())
                    .key("nota").value(treino.getNota())
                    .key("situacao").value(treino.getSituacao())
                    .key("emailCorredor").value(treino.getEmailCorredor())
                    .key("emailTreinador").value(treino.getEmailTreinador())
                    .key("dtCadastro").value(dateFormat.format(treino.getDtCadastro()))

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }

    public String TreinoCompletoToJson(Treino treino) {
        // Log.i("treino json", treino.toString());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()

                    .key("codTreino").value(treino.getCodTreino())
                    .key("nome").value(treino.getNome())
                    .key("qtdSemanas").value(treino.getQtdSemanas())
                    .key("qtdDias").value(treino.getQtdSemanas())
                    .key("obs").value(treino.getObservacao())
                    .key("obj").value(treino.getObjetivo())
                    .key("dtInicio").value(sqlDateFormat.format(treino.getDtInicio()))
                    .key("dtFim").value(sqlDateFormat.format(treino.getDtFim()))
                    .key("avaliacao").value(treino.getAvaliacaoDoCorredor())
                    .key("nota").value(treino.getNota())
                    .key("situacao").value(treino.getSituacao())
                    .key("emailCorredor").value(treino.getEmailCorredor())
                    .key("emailTreinador").value(treino.getEmailTreinador())
                    .key("dtCadastro").value(dateFormat.format(treino.getDtCadastro()))
                    .key("treinoExercicios").array();


            List<TreinoExercicio> treinoExercicios =  treino.getTreinoExercicios();

            for (TreinoExercicio treinoExercicio : treinoExercicios) {

                jsonStringer.object()
                        .key("semana").value(treinoExercicio.getSemana())
                        .key("data").value(dateFormat.format(treinoExercicio.getData()))
                        .key("ritmo").value(treinoExercicio.getRitmo())
                        .key("volume").value(treinoExercicio.getVolume())
                        .key("ordem").value(treinoExercicio.getOrdem())
                        .key("qtdRepeticoes").value(treinoExercicio.getQtdRepeticoes())
                        .key("intervaloRepeticoes").value(treinoExercicio.getIntervaloRepeticoes())
                        .key("posIntervalo").value(treinoExercicio.getPosIntervalo())
                        .key("codExercicio").value(treinoExercicio.getExercicio().getCodExercicio())
                        .key("codTreino").value(treinoExercicio.getCodTreino())
                        .key("codCorrida").value(treinoExercicio.getCodCorrida())

                        .endObject();
            }

            jsonStringer.endArray().endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json = ", jsonStringer.toString());
        return jsonStringer.toString();
    }


    public Treino JsonToTreino(String data) {
        Treino treino = new Treino();

        try {
            JSONObject jo = new JSONObject(data);

            treino.setCodTreino(jo.getInt("codTreino"));
            treino.setNome(jo.getString("nome"));
            treino.setQtdSemanas(jo.getInt("qtdSemanas"));

            if(!(jo.getString("emailCorredor").equals("null"))) {
                treino.setEmailCorredor(jo.getString("emailCorredor"));
            }
            if(!(jo.getString("emailTreinador").equals("null"))) {
                treino.setEmailTreinador(jo.getString("emailTreinador"));
            }
            if(!(jo.getString("avaliacao").equals("null"))) {
                treino.setAvaliacaoDoCorredor(jo.getString("avaliacao"));
            } else{
                treino.setAvaliacaoDoCorredor("");
            }
            if(!(jo.getString("nota").equals("null"))) {
                treino.setNota(jo.getInt("nota"));
            }
            if(!(jo.getString("situacao").equals("null"))) {
                treino.setSituacao(jo.getString("situacao"));
            }
            if(!(jo.getString("obj").equals("null"))) {
                treino.setObjetivo(jo.getInt("obj"));
            }
            if(!(jo.getString("obs").equals("null"))) {
                treino.setObservacao(jo.getString("obs"));
            }
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if(!(jo.getString("dtInicio").equals("null"))) {
                treino.setDtInicio(format.parse(jo.getString("dtInicio")));
            }
            if(!(jo.getString("dtFim").equals("null"))) {
                treino.setDtFim(format.parse(jo.getString("dtFim")));
            }
            if(!(jo.getString("dtCadastro").equals("null"))) {
                treino.setDtCadastro(format.parse(jo.getString("dtCadastro")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        

        return treino;
    }

    public List JsonArrayToListaTreino(String data) {
        List<Treino> list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {
                Treino treino = JsonToTreino(json.getJSONObject(i).toString());
                list.add(treino);
                Log.i("Treino" + i + " ", treino.getNome());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }

    public List JsonAssiduidadeListaTreino(List<Treino> mList, String data){


        try {
            JSONArray json = new JSONArray(data);

            for (int i = 0; i < json.length(); i++) {

                for (Treino treino: mList){

                    if (treino.getCodTreino() == json.getJSONObject(i).getInt("COD_TREINO")){
                        treino.setDiasTotais(json.getJSONObject(i).getInt("DIAS_TOTAIS"));
                        if(!json.getJSONObject(i).getString("DIAS_REALIZADOS").equals("null")){
                            treino.setDiasRealizados(json.getJSONObject(i).getInt("DIAS_REALIZADOS"));
                        } else{
                            treino.setDiasRealizados(0);
                        }
                        Log.i("Assiduidade" + i + " ", treino.getNome());
                    }

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



        return mList;
    }



}
