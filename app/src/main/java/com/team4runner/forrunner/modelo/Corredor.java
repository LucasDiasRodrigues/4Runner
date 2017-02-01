package com.team4runner.forrunner.modelo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.team4runner.forrunner.tasks.BuscaConversasTask;
import com.team4runner.forrunner.tasks.DesassociarTask;
import com.team4runner.forrunner.tasks.ListaCorridasTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 01/11/2014.
 */


public class Corredor extends Usuario implements Serializable {


    private double peso;
    private double altura;
    private double pulsorepouso;
    private int objetivo;
    private String emailTreinador;


    public void desassociarDeTreinador(Activity activity) {
        DesassociarTask task = new DesassociarTask(activity, this);
        task.execute();
    }


    public void buscaConversas(Activity activity) {
        BuscaConversasTask task = new BuscaConversasTask(activity, this);
        task.execute();
    }

    public void buscaCorridasLivres(Activity activity, Fragment fragment) {
        List<Corrida> mList = new ArrayList<>();
        ListaCorridasTask task = new ListaCorridasTask(activity, this, "livre", fragment);
        task.execute();

    }

    public List<Corredor> ordenarDistancia(List<Corredor> corredoresGeo) {
        List<Corredor> corredoresOrdenados = new ArrayList<>();

        Corredor aux;

        for (int i = 0; i < corredoresGeo.size(); i++) {

            for (int j = 0; j < (corredoresGeo.size() - 1); j++) {
                if (Float.parseFloat(corredoresGeo.get(j).getSituacao()) > Float.parseFloat(corredoresGeo.get(j + 1).getSituacao())) {
                    aux = corredoresGeo.get(j);
                    corredoresGeo.set(j, corredoresGeo.get(j + 1));
                    corredoresGeo.set(j + 1, aux);
                }
            }
        }
        corredoresOrdenados.addAll(corredoresGeo);
        return corredoresOrdenados;
    }


    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPulsorepouso() {
        return pulsorepouso;
    }

    public void setPulsorepouso(double pulsorepouso) {
        this.pulsorepouso = pulsorepouso;
    }

    public int getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(int objetivo) {
        this.objetivo = objetivo;
    }


    public String getEmailTreinador() {
        return emailTreinador;
    }

    public void setEmailTreinador(String emailTreinador) {
        this.emailTreinador = emailTreinador;
    }


    @Override
    public String toString() {
        return super.getNome() + "";
    }


}
