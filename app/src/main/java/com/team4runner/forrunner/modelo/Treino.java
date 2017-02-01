package com.team4runner.forrunner.modelo;

import android.app.Activity;

import com.team4runner.forrunner.tasks.AtribuirNotaTreinoTask;
import com.team4runner.forrunner.tasks.AvaliarTreinoCorredorTask;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Lucas on 01/11/2014.
 */
public class Treino implements Serializable {
    private int codTreino;
    private String nome;
    private int qtdSemanas;
    private int qtdDias;
    private String observacao;
    private int objetivo;
    private Date dtInicio = new Date(0);
    private Date dtFim = new Date(0);
    private int nota;
    private String situacao;
    private Date dtCadastro = new Date(0);
    private String emailCorredor;
    private String emailTreinador;
    private String avaliacaoDoCorredor;
    private int diasTotais;
    private int diasRealizados;
    private List<TreinoExercicio> treinoExercicios;

    public void atribuirNotaTreinador(Activity activity){

        AtribuirNotaTreinoTask task = new AtribuirNotaTreinoTask(activity,this);
        task.execute();

    }

    public void avaliarTreinoCorredor(Activity activity){

        AvaliarTreinoCorredorTask task = new AvaliarTreinoCorredorTask(activity,this);
        task.execute();
    }


    public int getCodTreino() {
        return codTreino;
    }

    public void setCodTreino(int codTreino) {
        this.codTreino = codTreino;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdSemanas() {
        return qtdSemanas;
    }

    public void setQtdSemanas(int qtdSemanas) {
        this.qtdSemanas = qtdSemanas;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public String getEmailCorredor() {
        return emailCorredor;
    }

    public void setEmailCorredor(String emailCorredor) {
        this.emailCorredor = emailCorredor;
    }

    public String getEmailTreinador() {
        return emailTreinador;
    }

    public void setEmailTreinador(String emailTreinador) {
        this.emailTreinador = emailTreinador;
    }

    public String getAvaliacaoDoCorredor() {
        return avaliacaoDoCorredor;
    }

    public void setAvaliacaoDoCorredor(String avaliacaoDoCorredor) {
        this.avaliacaoDoCorredor = avaliacaoDoCorredor;
    }

    public List<TreinoExercicio> getTreinoExercicios() {
        return treinoExercicios;
    }

    public void setTreinoExercicios(List<TreinoExercicio> treinoExercicios) {
        this.treinoExercicios = treinoExercicios;
    }

    public int getQtdDias() {
        return qtdDias;
    }

    public void setQtdDias(int qtdDias) {
        this.qtdDias = qtdDias;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(int objetivo) {
        this.objetivo = objetivo;
    }

    public int getDiasTotais() {
        return diasTotais;
    }

    public void setDiasTotais(int diasTotais) {
        this.diasTotais = diasTotais;
    }

    public int getDiasRealizados() {
        return diasRealizados;
    }

    public void setDiasRealizados(int diasRealizados) {
        this.diasRealizados = diasRealizados;
    }
}

