package com.jack.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {
    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool JedisFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(500);
        poolConfig.setMaxIdle(100);
        poolConfig.setMinIdle(10);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestOnCreate(true);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(1000);
        JedisPool jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379,
                3000);
        return jedisPool;
    }
}
