package com.sa.pharmacies.common.infrastructure.security;

public interface JwtOutputPort {
    String getUsername(String token);
    String getRole(String token);
    boolean isValid(String token);
    void updateTokenExpiration(String username);
    boolean isTokenExpired(String username);
}
