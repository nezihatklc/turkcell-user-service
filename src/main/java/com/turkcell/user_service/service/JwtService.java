package com.turkcell.user_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

// JWT token oluşturma ve doğrulama işlemlerini yöneten servis
@Service
public class JwtService {

    // application.properties'den gizli anahtarı al
    @Value("${jwt.secret}")
    private String secret;

    // application.properties'den token süresini al
    @Value("${jwt.expiration}")
    private long expiration;

    // Gizli anahtarı Key nesnesine çevir
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Kullanıcı email'i için token üret
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)          // token'ın sahibi
                .setIssuedAt(new Date())     // token oluşturulma zamanı
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // bitiş zamanı
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // imzala
                .compact();
    }

    // Token'dan email'i çıkar
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Token geçerli mi kontrol et
    public boolean isTokenValid(String token, String email) {
        String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    // Token süresi dolmuş mu?
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Token'ın içindeki bilgileri (claims) çıkar
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}