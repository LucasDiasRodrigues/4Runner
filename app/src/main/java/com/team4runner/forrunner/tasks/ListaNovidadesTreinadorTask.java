package com.team4runner.forrunner.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.team4runner.forrunner.adapter.ListViewAdapterNovidades;
import com.team4runner.forrunner.fragment.HomeTreinadorNovidadesFragment;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;
import com.team4runner.forrunner.web.NovidadesTreinadorJson;

import java.util.List;

/**
 * Created by Lucas on 21/12/2015.
 */
public class ListaNovidadesTreinadorTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/lista_novidades_json.php";


    Context context;
    String emailTreinador;
    String tipo;
    boolean completa = false;

    String method;
    Treinador treinador;
    String data;
    TreinadorJson treinadorJson;
    String answer;

    NovidadesTreinadorJson novidadesTreinadorJson;
    List<Bundle> bundles;

    HomeTreinadorNovidadesFragment fragment;


    public ListaNovidadesTreinadorTask(Context context, String emailTreinador, String tipo, boolean completa) {
        this.context = context;
        this.emailTreinador = emailTreinador;
        this.tipo = tipo;
        this.completa = completa;
    }

    public ListaNovidadesTreinadorTask(Context context, String emailTreinador, String tipo, boolean completa, HomeTreinadorNovidadesFragment fragment) {
        this.context = context;
        this.emailTreinador = emailTreinador;
        this.tipo = tipo;
        this.completa = completa;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        switch (tipo) {
            case "treinoavencer":

                if (completa) {
                    method = "lista_treinosavencer_completa-json";
                } else {
                    method = "lista_treinosavencer-json";
                }

                treinador = new Treinador();
                treinador.setEmail(emailTreinador);
                treinadorJson = new TreinadorJson();
                data = treinadorJson.TreinadorToJson(treinador);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task 1", " " + answer);

                novidadesTreinadorJson = new NovidadesTreinadorJson();
                bundles = novidadesTreinadorJson.JsonToTreinosaVencer(answer);

                return bundles;


            case "treinoconcluido":

                if (completa) {
                    method = "lista_treinosconcluidos_completa-json";
                } else {
                    method = "lista_treinosconcluidos-json";
                }

                treinador = new Treinador();
                treinador.setEmail(emailTreinador);
                treinadorJson = new TreinadorJson();
                data = treinadorJson.TreinadorToJson(treinador);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task 2", " " + answer);

                novidadesTreinadorJson = new NovidadesTreinadorJson();
                bundles = novidadesTreinadorJson.JsonToTreinosConcluidos(answer);

                return bundles;

            case "ultimascorridas":

                if (completa) {
                    method = "lista_ultimascorridas_completa-json";
                } else {
                    method = "lista_ultimascorridas-json";
                }

                treinador = new Treinador();
                treinador.setEmail(emailTreinador);
                treinadorJson = new TreinadorJson();
                data = treinadorJson.TreinadorToJson(treinador);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task 3", " " + answer);

                novidadesTreinadorJson = new NovidadesTreinadorJson();
                bundles = novidadesTreinadorJson.JsonToUltimasCorridas(answer);

                return bundles;

            case "ultimostestes":


                if (completa) {
                    method = "lista_ultimostestes_completa-json";
                } else {
                    method = "lista_ultimostestes-json";
                }


                treinador = new Treinador();
                treinador.setEmail(emailTreinador);
                treinadorJson = new TreinadorJson();
                data = treinadorJson.TreinadorToJson(treinador);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task 4", " " + answer);

                novidadesTreinadorJson = new NovidadesTreinadorJson();
                bundles = novidadesTreinadorJson.JsonToUltimosTestes(answer);


                return bundles;

            case "novoscorredores":


                if (completa) {
                    method = "lista_novosCorredores_completa-json";
                } else {
                    method = "lista_novosCorredores-json";
                }


                treinador = new Treinador();
                treinador.setEmail(emailTreinador);
                treinadorJson = new TreinadorJson();
                data = treinadorJson.TreinadorToJson(treinador);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task 5", " " + answer);

                novidadesTreinadorJson = new NovidadesTreinadorJson();
                bundles = novidadesTreinadorJson.JsonToNovosCorredores(answer);


                return bundles;

            case "ausencias":


                if (completa) {
                    method = "lista_ausencias_treino_completa-json";
                } else {
                    method = "lista_ausencias_treino-json";
                }


                treinador = new Treinador();
                treinador.setEmail(emailTreinador);
                treinadorJson = new TreinadorJson();
                data = treinadorJson.TreinadorToJson(treinador);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task 6", " " + answer);

                novidadesTreinadorJson = new NovidadesTreinadorJson();
                bundles = novidadesTreinadorJson.JsonToListaAusencias(answer);


                return bundles;

            case "ausenciasteste":


                if (completa) {
                    method = "lista_ausencias_teste_completa-json";
                } else {
                    method = "lista_ausencias_teste-json";
                }

                treinador = new Treinador();
                treinador.setEmail(emailTreinador);
                treinadorJson = new TreinadorJson();
                data = treinadorJson.TreinadorToJson(treinador);

                answer = HttpConnection.getSetDataWeb(this.url, method, data);
                Log.i("Task 7", " " + answer);

                novidadesTreinadorJson = new NovidadesTreinadorJson();
                bundles = novidadesTreinadorJson.JsonToListaAusenciasTeste(answer);


                return bundles;
        }


        return null;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (o != null) {

            if (this.fragment != null) {

                switch (tipo) {
                    case "treinoavencer":

                        fragment.atualizaTreinosaVencer((List<Bundle>)o);

                        break;

                    case "treinoconcluido":

                        fragment.atualizaTreinosConcluidos((List<Bundle>) o);
                        break;

                    case "ultimascorridas":

                        fragment.atualizaUltimasCorridas((List<Bundle>) o);
                        break;

                    case "ultimostestes":

                        fragment.atualizaUltimosTestes((List<Bundle>) o);
                        break;

                    case "novoscorredores":

                        fragment.atualizaNovosCorredores((List<Bundle>) o);
                        break;

                    case "ausencias":

                        fragment.atualizaListaAusencia((List<Bundle>) o);
                        break;

                    case "ausenciasteste":

                        fragment.atualizaListaAusenciaTeste((List<Bundle>)o);
                        break;

                }
            }
        }

    }
}
