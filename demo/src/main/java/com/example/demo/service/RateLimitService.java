package com.example.demo.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    public RateLimitService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allowRequest(String userId, String apiName) {
        String key = userId + ":" + apiName;
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, 60, TimeUnit.SECONDS);
        }
        return count <= 10000;
    }
}