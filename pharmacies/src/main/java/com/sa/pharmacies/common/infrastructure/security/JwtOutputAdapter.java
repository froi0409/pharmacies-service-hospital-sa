package com.sa.pharmacies.common.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtOutputAdapter implements JwtOutputPort {
    private final long EXPIRATION_TIME = 86400000; // 10 days
    private final int MINUTES_ACTIVE = 5;
    private final String SECRET;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtOutputAdapter(@Value("${jwt.secret}") String secret, RedisTemplate<String, String> redisTemplate) {
        this.SECRET = secret;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    @Override
    public String getRole(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class);
    }

    @Override
    public boolean isValid(String token) {
        try {
            String username = getUsername(token);
            return !isTokenExpired(username);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateTokenExpiration(String username) {
        redisTemplate.opsForValue().set(username, "active", MINUTES_ACTIVE, TimeUnit.MINUTES);
    }

    @Override
    public boolean isTokenExpired(String username) {
        Boolean exists = redisTemplate.hasKey(username);
        return (exists == null || !exists);
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
