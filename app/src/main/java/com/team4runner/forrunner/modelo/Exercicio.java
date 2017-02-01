package com.team4runner.forrunner.modelo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.team4runner.forrunner.CadastroExercicioActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.fragment.ExerciciosTreinadorFragment;
import com.team4runner.forrunner.tasks.CadastroExercicioTask;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 01/11/2014.
 */
public class Exercicio implements Serializable {

    private int codExercicio;
    private String nomeExercicio;
    private String descricaoExercicio;
    private String emailTreinador;
    private Date dataCadastro;
    private String situacao;


    public boolean cadastrarExercicio(Context context, ExerciciosTreinadorFragment fragment) {
        CadastroExercicioTask task = new CadastroExercicioTask(context, this, "cadastrar");
        task.execute();
        try {
            String aux = task.get();
            if (aux.equals("cadastrarsucesso")) {
                fragment.listaExercicios();
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarExercicio(Context context) {

        CadastroExercicioTask task = new CadastroExercicioTask(context, this, "editar");
        task.execute();

        try {
            String aux = task.get();
            if (aux.equals("editarsucesso")) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void inativarExercicio(Context context, ExerciciosTreinadorFragment fragment) {

        CadastroExercicioTask task = new CadastroExercicioTask(context, this, "inativar");
        task.execute();

        try {
            String aux = task.get();
            if (aux.equals("inativarsucesso")) {
                fragment.listaExercicios();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }




    public String toString() {
        return (this.getNomeExercicio());
    }

    public int getCodExercicio() {
        return codExercicio;
    }

    public void setCodExercicio(int codExercicio) {
        this.codExercicio = codExercicio;
    }

    public String getNomeExercicio() {
        return nomeExercicio;
    }

    public void setNomeExercicio(String nomeexercicio) {
        this.nomeExercicio = nomeexercicio;
    }

    public String getDescricaoExercicio() {
        return descricaoExercicio;
    }

    public void setDescricaoExercicio(String descricaoExercicio) {
        this.descricaoExercicio = descricaoExercicio;
    }

    public String getEmailTreinador() {
        return emailTreinador;
    }

    public void setEmailTreinador(String emailTreinador) {
        this.emailTreinador = emailTreinador;
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

}
