package com.example.springsecurityjwtapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtGeneratorTest {

    @Test
    void validateToken(){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String username = "user";

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt( new Date())
                .setExpiration(expireDate)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
        System.out.println(token);


        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",ex.fillInStackTrace());
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String usernameGot = claims.getSubject();

    }

}
