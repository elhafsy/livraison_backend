package com.livraison.livraison.security.jwt;



import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.Jwts.*;


@Component
public class JwtUtils {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Utilisation du paramètre configurable
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT expiré : " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT non supporté : " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("JWT mal formé : " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Signature JWT invalide : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Token JWT vide : " + e.getMessage());
        }
        return false;
    }

}
