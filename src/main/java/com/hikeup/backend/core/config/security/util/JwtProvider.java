package com.hikeup.backend.core.config.security.util;

import com.hikeup.backend.core.config.exception.exceptions.JwtExpiredOrUntrustedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Project: hikeup-backend
 *
 * @author : sojer
 * @since : 22.01.2024
 **/
@Component
public class JwtProvider {

    private final AuthPropertiesProvider authPropertiesProvider;

    public JwtProvider(AuthPropertiesProvider authPropertiesProvider) {
        this.authPropertiesProvider = authPropertiesProvider;
    }

    public String generateToken(Map<String, Object> extraClaims, String username, Long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception exc) {
            throw new JwtExpiredOrUntrustedException(exc.getMessage());
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(authPropertiesProvider.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token) && isTokenValidWithSecretKey(token);
        } catch (JwtException exc) {
            return false;
        }
    }

    public boolean isTokenValidWithSecretKey(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException exc) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
