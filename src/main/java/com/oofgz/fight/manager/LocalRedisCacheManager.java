package com.oofgz.fight.manager;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;

import java.util.Map;

public class LocalRedisCacheManager extends RedisCacheManager {

    private final RedisConnectionFactory connectionFactory;
    private final Cache localCache;
    private final RedisOperations redisOperations;
    private final String topicName;

    public LocalRedisCacheManager(RedisCacheWriter cacheWriter,
                                  RedisCacheConfiguration defaultCacheConfiguration,
                                  RedisConnectionFactory connectionFactory,
                                  Cache localCache,
                                  RedisOperations redisOperations,
                                  String topicName) {
        super(cacheWriter, defaultCacheConfiguration);


        this.connectionFactory = connectionFactory;
        this.localCache = localCache;
        this.redisOperations = redisOperations;
        this.topicName = topicName;

        check();
    }

    public LocalRedisCacheManager(RedisCacheWriter cacheWriter,
                                  RedisCacheConfiguration defaultCacheConfiguration,
                                  RedisConnectionFactory connectionFactory,
                                  Cache localCache,
                                  RedisOperations redisOperations,
                                  String topicName,
                                  String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);


        this.connectionFactory = connectionFactory;
        this.localCache = localCache;
        this.redisOperations = redisOperations;
        this.topicName = topicName;

        check();
    }

    public LocalRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                  Map<String, RedisCacheConfiguration> initialCacheConfigurations,
                                  RedisConnectionFactory connectionFactory,
                                  Cache localCache,
                                  RedisOperations redisOperations,
                                  String topicName) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);

        this.connectionFactory = connectionFactory;
        this.localCache = localCache;
        this.redisOperations = redisOperations;
        this.topicName = topicName;

        check();
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new LocalRedisCache(name, RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                cacheConfig != null ? cacheConfig : RedisCacheConfiguration.defaultCacheConfig(),
                localCache, redisOperations, topicName);
    }

    public static LocalRedisCacheManager create(RedisConnectionFactory connectionFactory, Cache localCache, RedisOperations redisOperations, String topicName) {

        Assert.notNull(localCache, "localCache must not be null");
        Assert.notNull(connectionFactory, "connectionFactory must not be null");
        Assert.notNull(redisOperations, "redisOperations must not be null");
        Assert.notNull(topicName, "topicName must not be null");
        return new LocalRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                RedisCacheConfiguration.defaultCacheConfig(), connectionFactory, localCache, redisOperations, topicName);
    }


    private void check() {
        Assert.notNull(localCache, "localCache must not be null");
        Assert.notNull(connectionFactory, "connectionFactory must not be null");
        Assert.notNull(redisOperations, "redisOperations must not be null");
        Assert.notNull(topicName, "topicName must not be null");
    }


}
