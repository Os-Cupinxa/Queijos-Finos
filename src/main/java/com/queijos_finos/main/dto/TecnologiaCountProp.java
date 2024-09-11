package com.queijos_finos.main.dto;

public class TecnologiaCountProp {
	private String nome;
    private Long qtd;

    public TecnologiaCountProp(String nome, Long qtd) {
        this.nome = nome;
        this.qtd = qtd;
    }

    // Getters and setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getQtd() {
        return qtd;
    }

    public void setQtd(Long qtd) {
        this.qtd = qtd;
    }
}
