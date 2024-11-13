package com.queijos_finos.main.dto;

import java.util.Objects;

public class LogInDTO {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogInDTO logInDTO = (LogInDTO) o;
        return Objects.equals(email, logInDTO.email) && Objects.equals(password, logInDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
