package com.queijos_finos.main.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private Date data;
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return Objects.equals(id, agenda.id) && Objects.equals(descricao, agenda.descricao) && Objects.equals(data, agenda.data) && Objects.equals(tipo, agenda.tipo) && Objects.equals(usuario, agenda.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, data, tipo, usuario);
    }

    @Override
    public String toString() {
        return "Agenda{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", data=" + data +
                ", tipo='" + tipo + '\'' +
                ", usuario=" + usuario +
                '}';
    }
}
