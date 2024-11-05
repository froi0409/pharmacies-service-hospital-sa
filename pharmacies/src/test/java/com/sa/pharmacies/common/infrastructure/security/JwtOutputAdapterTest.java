package com.sa.pharmacies.common.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtOutputAdapterTest {

    private static final String SECRET = "base64EncodedSecretKeyThatIsAtLeast256BitsLongForTestingPurposes";
    private static final String USERNAME = "testUser";
    private static final String ROLE = "ADMIN";
    private static final String TOKEN = "testToken";

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private JwtOutputAdapter jwtOutputAdapter;

    @BeforeEach
    public void setUp() {
        jwtOutputAdapter = new JwtOutputAdapter(SECRET, redisTemplate);
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testGetUsername() {
        // Arrange
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", USERNAME);
        String token = Jwts.builder()
                .setClaims(claimsMap)
                .signWith(jwtOutputAdapter.getSecretKey())
                .compact();

        // Act
        String result = jwtOutputAdapter.getUsername(token);

        // Assert
        assertEquals(USERNAME, result);
    }

    @Test
    public void testGetRole() {
        // Arrange
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", USERNAME);
        claimsMap.put("role", ROLE);
        String token = Jwts.builder()
                .setClaims(claimsMap)
                .signWith(jwtOutputAdapter.getSecretKey())
                .compact();

        // Act
        String result = jwtOutputAdapter.getRole(token);

        // Assert
        assertEquals(ROLE, result);
    }

    @Test
    public void testIsValid_ValidToken() {
        // Arrange
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", USERNAME);
        String token = Jwts.builder()
                .setClaims(claimsMap)
                .signWith(jwtOutputAdapter.getSecretKey())
                .compact();
        when(redisTemplate.hasKey(USERNAME)).thenReturn(true);

        // Act
        boolean isValid = jwtOutputAdapter.isValid(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidToken() {
        // Arrange
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", USERNAME);
        String token = Jwts.builder()
                .setClaims(claimsMap)
                .signWith(jwtOutputAdapter.getSecretKey())
                .compact();
        when(redisTemplate.hasKey(USERNAME)).thenReturn(false);

        // Act
        boolean isValid = jwtOutputAdapter.isValid(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testUpdateTokenExpiration() {
        // Arrange
        doNothing().when(valueOperations).set(USERNAME, "active", 15L, TimeUnit.MINUTES);

        // Act
        jwtOutputAdapter.updateTokenExpiration(USERNAME);

        // Assert
        verify(valueOperations).set(USERNAME, "active", 15L, TimeUnit.MINUTES);
    }

    @Test
    public void testIsTokenExpired_NotExpired() {
        // Arrange
        when(redisTemplate.hasKey(USERNAME)).thenReturn(true);

        // Act
        boolean isExpired = jwtOutputAdapter.isTokenExpired(USERNAME);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    public void testIsTokenExpired_Expired() {
        // Arrange
        when(redisTemplate.hasKey(USERNAME)).thenReturn(false);

        // Act
        boolean isExpired = jwtOutputAdapter.isTokenExpired(USERNAME);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    public void testGetSecretKey() {
        // Act
        SecretKey secretKey = jwtOutputAdapter.getSecretKey();

        // Assert
        assertNotNull(secretKey);
        assertEquals(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET)), secretKey);
    }

    @Test
    public void testExtractClaims() {
        // Arrange
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("sub", USERNAME);
        String token = Jwts.builder()
                .setClaims(claimsMap)
                .signWith(jwtOutputAdapter.getSecretKey())
                .compact();

        // Act
        Claims extractedClaims = jwtOutputAdapter.extractClaims(token);

        // Assert
        assertEquals(USERNAME, extractedClaims.getSubject());
    }
}
