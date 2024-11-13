package com.queijos_finos.main.dto;

import java.util.Objects;

public class SingUpDTO {

    private String nome;

    private String email;

    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingUpDTO singUpDTO = (SingUpDTO) o;
        return Objects.equals(nome, singUpDTO.nome) && Objects.equals(email, singUpDTO.email) && Objects.equals(senha, singUpDTO.senha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email, senha);
    }
}
