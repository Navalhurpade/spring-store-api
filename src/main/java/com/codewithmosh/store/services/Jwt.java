package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.util.Date;

@AllArgsConstructor
@Getter
public class Jwt {
    private final SecretKey key;
    private final Claims claims;
    
    public boolean isValid() {
        return claims.getExpiration().after(new Date());
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    public String toString() {
        return Jwts.builder().claims(claims).signWith(key).compact();
    }
}
