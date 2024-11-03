package com.sa.pharmacies.common.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class RestTemplateConfig {

    private final AuthorizationHeaderInterceptor authInterceptor;

    public RestTemplateConfig(AuthorizationHeaderInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // Agregar el interceptor al RestTemplate
        restTemplate.getInterceptors().add(authInterceptor);
        return restTemplate;
    }
}