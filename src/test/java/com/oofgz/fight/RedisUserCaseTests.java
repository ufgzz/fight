package com.oofgz.fight;

import com.oofgz.fight.dto.redis.RedisUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisUserCaseTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, RedisUser> redisUserRedisTemplate;


    @Before
    public void setUp() {
        log.info("测试Redis的功能");
    }

    @Test
    public void redisUserTest() {
        // 保存字符串
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

        // 保存对象
        RedisUser user = new RedisUser("超人", 20);
        redisUserRedisTemplate.opsForValue().set(user.getUsername(), user);

        user = new RedisUser("蝙蝠侠", 30);
        redisUserRedisTemplate.opsForValue().set(user.getUsername(), user);

        user = new RedisUser("蜘蛛侠", 40);
        redisUserRedisTemplate.opsForValue().set(user.getUsername(), user);

        Assert.assertEquals(20, redisUserRedisTemplate.opsForValue().get("超人").getAge().longValue());
        Assert.assertEquals(30, redisUserRedisTemplate.opsForValue().get("蝙蝠侠").getAge().longValue());
        Assert.assertEquals(40, redisUserRedisTemplate.opsForValue().get("蜘蛛侠").getAge().longValue());

    }

}
