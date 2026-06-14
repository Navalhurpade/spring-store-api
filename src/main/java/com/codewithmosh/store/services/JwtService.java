package com.codewithmosh.store.services;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    private Jwt generateToken(User user, int tokenExpiration) {
        var claims = Jwts.claims()
                .add("name", user.getName())
                .add("email", user.getEmail())
                .add("role", user.getRole())
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date((System.currentTimeMillis() + 1000L * tokenExpiration)))
                .build();

        return new Jwt(jwtConfig.getSecretKey(), claims);
    }

    public Jwt parse(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(jwtConfig.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return new Jwt(jwtConfig.getSecretKey(), claims);
        } catch (JwtException e) {
            return null;
        }
    }

    public Jwt generateAccessToken(User user) {
        var jwt = generateToken(user, jwtConfig.getAccessTokenExpiration());
        var token = Jwts.builder()
                .claims(jwt.getClaims())
                .signWith(jwtConfig.getSecretKey())
                .compact();
        return parse(token);
    }

    public Jwt generateRefreshToken(User user) {
        var jwt = generateToken(user, jwtConfig.getRefreshTokenExpiration());
        var token = Jwts.builder()
                .claims(jwt.getClaims())
                .signWith(jwtConfig.getSecretKey())
                .compact();
        return parse(token);
    }
}
