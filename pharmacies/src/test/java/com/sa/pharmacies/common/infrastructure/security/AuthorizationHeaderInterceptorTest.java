package com.sa.pharmacies.common.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorizationHeaderInterceptorTest {

    private static final String AUTH_TOKEN = "Bearer sample-auth-token";

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private ClientHttpRequestExecution clientHttpRequestExecution;

    @Mock
    private ClientHttpResponse clientHttpResponse;

    @InjectMocks
    private AuthorizationHeaderInterceptor authorizationHeaderInterceptor;

    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    public void setUp() {
        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, AUTH_TOKEN);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));
    }

    @Test
    public void testIntercept_AddsAuthorizationHeader() throws IOException {
        // Arrange
        when(clientHttpRequestExecution.execute(any(HttpRequest.class), any(byte[].class))).thenReturn(clientHttpResponse);
        when(httpRequest.getHeaders()).thenReturn(new HttpHeaders());

        // Act
        ClientHttpResponse response = authorizationHeaderInterceptor.intercept(httpRequest, new byte[0], clientHttpRequestExecution);

        // Assert
        assertNotNull(response);
        assertTrue(httpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION));
        assertEquals(AUTH_TOKEN, httpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
    }

    @Test
    public void testIntercept_NoAuthorizationHeader() throws IOException {
        // Arrange
        mockHttpServletRequest.removeHeader(HttpHeaders.AUTHORIZATION);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));
        when(clientHttpRequestExecution.execute(any(HttpRequest.class), any(byte[].class))).thenReturn(clientHttpResponse);
        when(httpRequest.getHeaders()).thenReturn(new HttpHeaders());

        // Act
        ClientHttpResponse response = authorizationHeaderInterceptor.intercept(httpRequest, new byte[0], clientHttpRequestExecution);

        // Assert
        assertNotNull(response);
        assertFalse(httpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION));
    }

    @Test
    public void testIntercept_NoRequestAttributes() throws IOException {
        // Arrange
        RequestContextHolder.resetRequestAttributes(); // Eliminar el contexto de solicitud
        when(clientHttpRequestExecution.execute(any(HttpRequest.class), any(byte[].class))).thenReturn(clientHttpResponse);
        when(httpRequest.getHeaders()).thenReturn(new HttpHeaders());

        // Act
        ClientHttpResponse response = authorizationHeaderInterceptor.intercept(httpRequest, new byte[0], clientHttpRequestExecution);

        // Assert
        assertNotNull(response);
        assertFalse(httpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION));
    }
}
