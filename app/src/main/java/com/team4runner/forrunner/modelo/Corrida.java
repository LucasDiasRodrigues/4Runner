package com.team4runner.forrunner.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lucas on 09/10/2015.
 */
public class Corrida implements Serializable {

    private int codCorrida;
    private String emailCorredor;
    private Date dtHoraInicio = new Date();
    private Date dtHoraFim = new Date();
    private float velocidadeMedia;
    private float velocidadeMax;
    private int temperatura;
    private float distancia;
    private int pSE;
    private String obs;
    private String clima;
    private String terreno;
    private double calorias;
    private float ritmoMedio;
    private String tipo;
    private ArrayList<PontoRota> rota;

    public Corrida(){
        rota = new ArrayList<PontoRota>();


    }


    public int getCodCorrida() {
        return codCorrida;
    }

    public void setCodCorrida(int codCorrida) {
        this.codCorrida = codCorrida;
    }

    public Date getDtHoraInicio() {
        return dtHoraInicio;
    }

    public void setDtHoraInicio(Date dtHoraInicio) {
        this.dtHoraInicio = dtHoraInicio;
    }

    public Date getDtHoraFim() {
        return dtHoraFim;
    }

    public void setDtHoraFim(Date dtHoraFim) {
        this.dtHoraFim = dtHoraFim;
    }

    public float getVelocidadeMedia() {
        return velocidadeMedia;
    }

    public void setVelocidadeMedia(float velocidadeMedia) {
        this.velocidadeMedia = velocidadeMedia;
    }

    public float getVelocidadeMax() {
        return velocidadeMax;
    }

    public void setVelocidadeMax(float velocidadeMax) {
        this.velocidadeMax = velocidadeMax;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getpSE() {
        return pSE;
    }

    public void setpSE(int pSE) {
        this.pSE = pSE;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTerreno() {
        return terreno;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public float getRitmoMedio() {
        return ritmoMedio;
    }

    public void setRitmoMedio(float ritmoMedio) {
        this.ritmoMedio = ritmoMedio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<PontoRota> getRota() {
        return rota;
    }

    public void setRota(ArrayList<PontoRota> pontoRotas) {
        this.rota = pontoRotas;
    }

    public void addPontoRota(PontoRota ponto){
        this.rota.add(ponto);
    }

    public String getEmailCorredor() {
        return emailCorredor;
    }

    public void setEmailCorredor(String emailCorredor) {
        this.emailCorredor = emailCorredor;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }
}
