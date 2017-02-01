package com.team4runner.forrunner.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lucas on 25/08/2015.
 */
public class TreinoExercicio implements Serializable {

    private int semana;
    private Date data = new Date();
    private float ritmo;
    private float volume;
    private int ordem;
    private int qtdRepeticoes = 1;
    private float intervaloRepeticoes;
    private int posIntervalo = 0;
    private Exercicio exercicio;
    private int codTreino;
    private int codCorrida;


    public int getSemana() {
        return semana;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public float getRitmo() {
        return ritmo;
    }

    public void setRitmo(float ritmo) {
        this.ritmo = ritmo;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getQtdRepeticoes() {
        return qtdRepeticoes;
    }

    public void setQtdRepeticoes(int qtdRepeticoes) {
        this.qtdRepeticoes = qtdRepeticoes;
    }

    public float getIntervaloRepeticoes() {
        return intervaloRepeticoes;
    }

    public void setIntervaloRepeticoes(float intervaloRepeticoes) {
        this.intervaloRepeticoes = intervaloRepeticoes;
    }

    public Exercicio getExercicio() {
        return exercicio;
    }

    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
    }

    public int getCodTreino() {
        return codTreino;
    }

    public void setCodTreino(int codTreino) {
        this.codTreino = codTreino;
    }

    public int getCodCorrida() {
        return codCorrida;
    }

    public void setCodCorrida(int codCorrida) {
        this.codCorrida = codCorrida;
    }

    public int getPosIntervalo() {
        return posIntervalo;
    }

    public void setPosIntervalo(int posIntervalo) {
        this.posIntervalo = posIntervalo;
    }
}
