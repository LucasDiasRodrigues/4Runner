package com.team4runner.forrunner.modelo;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lucas on 09/10/2015.
 */
public class PontoRota implements Serializable {

    private LatLng latLng;
    private Date dtHora;
    private float velocidade;
    private float distancia;
    private float tempo;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Date getDtHora() {
        return dtHora;
    }

    public void setDtHora(Date dtHora) {
        this.dtHora = dtHora;
    }

    public float getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(float velocidade) {
        this.velocidade = velocidade;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }
}
