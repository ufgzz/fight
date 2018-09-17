package com.oofgz.fight.config.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class MyCacheConfig {
    // 注释掉的 是我自己 分别配置几种缓存的时候用的 spring ioc 中只能有一个 CacheManager 实列，如果 有多个会报错。
    /*@Bean
    public CacheManager guavaCacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager.setCacheBuilder(CacheBuilder.newBuilder().expireAfterWrite(3600, TimeUnit.SECONDS).maximumSize(1000));
        return cacheManager;
    } */

   /* @Bean
    public CacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        return redisCacheManager;
    }*/




}
