package com.queijos_finos.main.utils;

import com.queijos_finos.main.model.JwtToken;
import com.queijos_finos.main.model.Usuarios;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public JwtToken generateToken(Usuarios user, long expirationMillis) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getEncoded());
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .claim("name", user.getNome())
                .claim("email", user.getEmail())
                .claim("userId", user.getIdUsuario())
                .signWith(key)
                .compact();
        return new JwtToken(token, expirationMillis);
    }

    public Claims extractClaims(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getEncoded());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getEncoded());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getTeamId(String token) {
        Claims claims = extractClaims(token);
        return claims.get("teamId", Long.class);
    }
    public String getUserProfile(String token) {
        Claims claims = extractClaims(token);
        return claims.get("profile", String.class);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaims(token).getExpiration();
        return expirationDate.before(new Date());
    }
}
