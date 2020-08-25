package com.jack.miaosha;

import com.jack.miaosha.redis.RedisPoolFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.JedisPool;

@SpringBootTest
class MiaoshaApplicationTests {

    @Autowired
    RedisPoolFactory redisPoolFactory;

    @Test
    void contextLoads() {
        JedisPool jedisPool = redisPoolFactory.JedisFactory();
        System.out.println(jedisPool.getResource());
    }

}
