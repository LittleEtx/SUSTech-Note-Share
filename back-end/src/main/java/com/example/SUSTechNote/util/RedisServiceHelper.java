package com.example.SUSTechNote.util;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis相关操作的工具类
 */
@Component
public class RedisServiceHelper {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisServiceHelper(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void put(String key, String value, long millis) {
        redisTemplate.opsForValue().set(key, value, millis, TimeUnit.MINUTES);
    }

    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
