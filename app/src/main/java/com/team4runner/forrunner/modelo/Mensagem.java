package com.team4runner.forrunner.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lucas on 01/12/2015.
 */
public class Mensagem implements Serializable {


    private String remetente;
    private String corredor;
    private String treinador;
    private String msg;
    private Date dthrEnviada;
    private Date dthrRecebida;
    private Date dthrVisualizada;
    private String situacao;
    private int codMsg;


    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDthrEnviada() {
        return dthrEnviada;
    }

    public void setDthrEnviada(Date dthrEnviada) {
        this.dthrEnviada = dthrEnviada;
    }

    public Date getDthrRecebida() {
        return dthrRecebida;
    }

    public void setDthrRecebida(Date dthrRecebida) {
        this.dthrRecebida = dthrRecebida;
    }

    public Date getDthrVisualizada() {
        return dthrVisualizada;
    }

    public void setDthrVisualizada(Date dthrVisualizada) {
        this.dthrVisualizada = dthrVisualizada;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getCorredor() {
        return corredor;
    }

    public void setCorredor(String corredor) {
        this.corredor = corredor;
    }

    public String getTreinador() {
        return treinador;
    }

    public void setTreinador(String treinador) {
        this.treinador = treinador;
    }

    public int getCodMsg() {
        return codMsg;
    }

    public void setCodMsg(int codMsg) {
        this.codMsg = codMsg;
    }
}
