package com.example.todo.config.redis;

import com.example.todo.model.dto.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final long EXPIRATION_TIME = 60;

    @Bean
    public RedisTemplate<String, UserResponseDto> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserResponseDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(ObjectMapper objectMapper) {
        objectMapper = objectMapper.copy();
        objectMapper = objectMapper.activateDefaultTypingAsProperty(requireNonNull(objectMapper).getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                "@class");
        ObjectMapper finalObjectMapper = objectMapper;
        return builder -> builder
                .withCacheConfiguration("userCache",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new GenericJackson2JsonRedisSerializer(finalObjectMapper)))
                                .entryTtl(Duration.ofMinutes(EXPIRATION_TIME))
                                .disableCachingNullValues());
    }
}
