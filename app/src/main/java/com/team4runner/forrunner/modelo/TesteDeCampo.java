package com.team4runner.forrunner.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lucas on 06/01/2016.
 */
public class TesteDeCampo implements Serializable{

    private int codTeste;
    private Double volume;
    private String tipoVolume;
    private Date dataTeste = new Date();
    private String obs;
    private int codCorrida;
    private String emailALuno;
    private String emailTreinador;
    private Date dataCadastro = new Date();
    private String situacao;

    public int getCodTeste() {
        return codTeste;
    }

    public void setCodTeste(int codTeste) {
        this.codTeste = codTeste;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getTipoVolume() {
        return tipoVolume;
    }

    public void setTipoVolume(String tipoVolume) {
        this.tipoVolume = tipoVolume;
    }

    public Date getDataTeste() {
        return dataTeste;
    }

    public void setDataTeste(Date dataTeste) {
        this.dataTeste = dataTeste;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public int getCodCorrida() {
        return codCorrida;
    }

    public void setCodCorrida(int codCorrida) {
        this.codCorrida = codCorrida;
    }

    public String getEmailALuno() {
        return emailALuno;
    }

    public void setEmailALuno(String emailALuno) {
        this.emailALuno = emailALuno;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getEmailTreinador() {
        return emailTreinador;
    }

    public void setEmailTreinador(String emailTreinador) {
        this.emailTreinador = emailTreinador;
    }
}
