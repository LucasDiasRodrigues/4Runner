package com.team4runner.forrunner.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lucas on 01/11/2014.
 */
public abstract class Usuario implements Serializable{


    private String nome;
    private Date dataNasc = new Date(0);
    private String telefone;
    private String email;
    private String sexo;
    private String sobreMim;
    private String imagemPerfil;
    private String senha;
    private String localizacao;
    private String locStatus;
    private String gcmId;
    private String situacao;
    private Date dataCadastro = new Date(0);




    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getImagemPerfil() {
       return imagemPerfil;
    }

    public void setImagemPerfil(String imagePerfil) {
        this.imagemPerfil = imagePerfil;
    }

    public String getSobreMim() {
        return sobreMim;
    }

    public void setSobreMim(String sobreMim) {
        this.sobreMim = sobreMim;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getLocStatus() {
        return locStatus;
    }

    public void setLocStatus(String locStatus) {
        this.locStatus = locStatus;
    }
}
