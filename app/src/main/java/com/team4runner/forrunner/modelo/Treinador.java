package com.team4runner.forrunner.modelo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import com.team4runner.forrunner.tasks.BuscaConversasTask;
import com.team4runner.forrunner.tasks.DesassociarTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 01/11/2014.
 */
public class Treinador extends Usuario implements Serializable {


    private String cref;
    private String formacao;
    private double mediaAvaliacao;
    private int qtdAvaliacoes;


    public void desassociarDeCorredor(Activity activity, Corredor corredor) {

        DesassociarTask task = new DesassociarTask(activity, corredor, this);
        task.execute();

    }

    public void buscaConversas(Activity activity) {
        BuscaConversasTask task = new BuscaConversasTask(activity, this);
        task.execute();
    }


    public List<Treinador> ordenarDistancia(List<Treinador> treinadoresGeo) {
        List<Treinador> treinadoresOrdenados = new ArrayList<>();

        Treinador aux;

        for (int i = 0; i < treinadoresGeo.size(); i++) {

            for (int j = 0; j < (treinadoresGeo.size() - 1); j++) {
                if (Float.parseFloat(treinadoresGeo.get(j).getSituacao()) > Float.parseFloat(treinadoresGeo.get(j + 1).getSituacao())) {
                    aux = treinadoresGeo.get(j);
                    treinadoresGeo.set(j, treinadoresGeo.get(j + 1));
                    treinadoresGeo.set(j + 1, aux);
                }
            }
        }
        treinadoresOrdenados.addAll(treinadoresGeo);
        return treinadoresOrdenados;
    }


    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    @Override
    public String toString() {
        return super.getNome() + "";
    }

    public double getMediaAvaliacao() {
        return mediaAvaliacao;
    }

    public void setMediaAvaliacao(double mediaAvaliacao) {
        this.mediaAvaliacao = mediaAvaliacao;
    }

    public int getQtdAvaliacoes() {
        return qtdAvaliacoes;
    }

    public void setQtdAvaliacoes(int qtdAvaliacoes) {
        this.qtdAvaliacoes = qtdAvaliacoes;
    }
}
