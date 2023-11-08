package com.example.springsecurityjwtapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerator {

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
        String secretKeyString = SecurityConstants.JWT_SECRET;

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .signWith(getSignatureSecretKey(secretKeyString))
                .compact();
    }

    public String getUsernameFromJWT(String token){
        String secretKeyString = SecurityConstants.JWT_SECRET;
        Claims claims = Jwts.parser()
                .setSigningKey(getSignatureSecretKey(secretKeyString))
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token){
        String secretKeyString = SecurityConstants.JWT_SECRET;
        try{
            Jwts.parser().setSigningKey(getSignatureSecretKey(secretKeyString)).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }

    public SecretKey getSignatureSecretKey(String secretKeyString){
        byte[] secretKeyBytes = secretKeyString.getBytes();
        return new SecretKeySpec(secretKeyBytes, "HmacHS512");
    }
}
