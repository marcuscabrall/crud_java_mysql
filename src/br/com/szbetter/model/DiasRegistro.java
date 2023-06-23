package br.com.szbetter.model;

import java.util.Date;

public class DiasRegistro {
    private int id;
    private Usuario usuario;
    private Date dataRegistro;
    private Date horaDormir;
    private Date horaAcordar;
    private String qualidadeSono;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Date getHoraDormir() {
        return horaDormir;
    }

    public void setHoraDormir(Date horaDormir) {
        this.horaDormir = horaDormir;
    }

    public Date getHoraAcordar() {
        return horaAcordar;
    }

    public void setHoraAcordar(Date horaAcordar) {
        this.horaAcordar = horaAcordar;
    }

    public String getQualidadeSono() {
        return qualidadeSono;
    }

    public void setQualidadeSono(String qualidadeSono) {
        this.qualidadeSono = qualidadeSono;
    }
}