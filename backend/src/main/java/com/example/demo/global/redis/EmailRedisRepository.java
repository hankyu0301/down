package com.example.demo.global.redis;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Repository
public class EmailRedisRepository {

    @Value("${spring.redis.email.expire-time}")
    private long EXPIRE_TIME_IN_MINUTES;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 6;

    public EmailRedisRepository(@Qualifier("emailRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void deleteVerification(String email) {
        redisTemplate.delete(email);
    }

    public String setVerification(String email) {
        deleteVerification(email);

        String code = generateVerification();

        redisTemplate.opsForValue().set(email, code);
        redisTemplate.expire(email, EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);

        return code;
    }

    public String getVerification(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }

    public boolean hasVerification(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(email));
    }

    public static String generateVerification() {
        StringBuilder codeBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char character = CHARACTERS.charAt(index);
            codeBuilder.append(character);
        }
        return codeBuilder.toString();
    }

}