package com.queijos_finos.main.model;

import java.util.Objects;

public class JwtToken {
    private String token;
    private Long expiration;

    public JwtToken(String token, Long expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtToken jwtToken = (JwtToken) o;
        return Objects.equals(token, jwtToken.token) && Objects.equals(expiration, jwtToken.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, expiration);
    }
}
