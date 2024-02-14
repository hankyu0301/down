package com.example.demo.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    /**
     * Chat Redis 설정
     * */
    @Value("${spring.redis.host.chat}")
    private String chatHost;

    @Value("${spring.redis.port.chat}")
    private int chatPort;

    @Value("${spring.redis.password.chat}")
    private String chatPassword;

    @Bean(name = "chatRedisConnectionFactory")
    public RedisConnectionFactory chatRedisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.setHostName(chatHost);
        connectionFactory.setPort(chatPort);
        connectionFactory.setPassword(chatPassword);
        return connectionFactory;
    }
}