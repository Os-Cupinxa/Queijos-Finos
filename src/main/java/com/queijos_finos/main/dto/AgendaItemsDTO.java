package com.queijos_finos.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AgendaItemsDTO {

    private String nome;
    private String descricao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date data;
    private String tipo;

    public AgendaItemsDTO(String nome, String descricao, Date data, String tipo) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
