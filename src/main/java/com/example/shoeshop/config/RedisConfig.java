package com.example.shoeshop.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    
    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = JsonMapper.builder()
                // Adding module for Java 8 Date/Time API support (Instant, LocalDate, etc.)
                .addModule(new JavaTimeModule())
                // Disable writing dates as timestamps (ISO-8601 strings will be used)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // Disable writing timezone information (important for Instant)
                .disable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                // Disable error on empty beans (important for Hibernate proxies)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        
        // Configure field visibility for serialization
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        
        // Removed global type information inclusion
        // mapper.activateDefaultTyping(...);
        
        return mapper;
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper redisObjectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Create serializer based on our configured ObjectMapper
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(redisObjectMapper, Object.class);
        
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }
}