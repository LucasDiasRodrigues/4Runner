package com.team4runner.forrunner.modelo;

/**
 * Created by Lucas on 05/11/2015.
 */
public class Objetivo {

    private int codObj;
    private String descricao;

    public int getCodObj() {
        return codObj;
    }

    public void setCodObj(int codObj) {
        this.codObj = codObj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        String aux = getDescricao()+"K";
        return aux;
    }
}
