package com.sa.pharmacies.common.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Configurar StringRedisSerializer para claves y valores
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer); // Claves
        template.setValueSerializer(stringRedisSerializer); // Valores
        template.setHashKeySerializer(stringRedisSerializer); // Claves de Hash
        template.setHashValueSerializer(stringRedisSerializer); // Valores de Hash

        template.afterPropertiesSet();
        return template;
    }
}
