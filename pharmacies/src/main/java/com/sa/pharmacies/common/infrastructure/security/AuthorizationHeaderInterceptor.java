package com.sa.pharmacies.common.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;

@Component
public class AuthorizationHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // Obtener el HttpServletRequest actual
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest currentRequest = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            if (currentRequest != null) {
                String authHeader = currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
                if (authHeader != null) {
                    // Agregar el encabezado Authorization a la solicitud saliente
                    request.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
                }
            }
        }

        return execution.execute(request, body);
    }
}
