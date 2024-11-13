package com.queijos_finos.main.dto;

import java.util.Objects;

public class LogInDTO {
    private String email;
    private String senha;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogInDTO logInDTO = (LogInDTO) o;
        return Objects.equals(email, logInDTO.email) && Objects.equals(senha, logInDTO.senha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, senha);
    }
}
