package com.oofgz.fight.manager;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/**
 * extends RedisCache，增加本地一级缓存，redis作为二级缓存
 */
public class LocalRedisCache extends RedisCache {

    private final Cache localCache;//本地一级缓存
    private final RedisOperations redisOperations;//配合topicName，发布缓存更新消息
    private final String topicName;//redis topic ，发布缓存更新消息通知其他client更新缓存

    /**
     * Create new {@link RedisCache}.
     *
     * @param name            must not be {@literal null}.
     * @param cacheWriter     must not be {@literal null}.
     * @param cacheConfig     must not be {@literal null}.
     * @param localCache      must not be {@literal null}.
     * @param redisOperations must not be {@literal null}.
     * @param topicName       must not be {@literal null}.
     */
    protected LocalRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig,
                              Cache localCache, RedisOperations redisOperations, String topicName) {

        super(name, cacheWriter, cacheConfig);

        Assert.notNull(localCache, "localCache must not be null!");
        Assert.notNull(redisOperations, "redisOperations must not be null!");
        Assert.hasText(topicName, "topicName must not be empty!");

        this.localCache = localCache;
        this.redisOperations = redisOperations;
        this.topicName = topicName;
    }

    @Override
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        //先读取本地一级缓存
        T value = localCache.get(key, valueLoader);
        if (value == null) {
            //本地一级缓存不存在，读取redis二级缓存
            value = super.get(key, valueLoader);
            if (value != null) {
                //redis二级缓存存在，存入本地一级缓存
                localCache.put(key, value);
                //发布缓存更新消息通知其他client更新缓存
                pub(new UpdateMessage(key, value, UpdateMessage.Type.PUT));
            }
        }
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
        localCache.put(key, value);
        pub(new UpdateMessage(key, value, UpdateMessage.Type.PUT));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper vw1 = localCache.putIfAbsent(key, value);
        ValueWrapper vw2 = super.putIfAbsent(key, value);

        pub(new UpdateMessage(key, value, UpdateMessage.Type.PUTIFABSENT));

        return vw1 == null ? vw2 : vw1;
    }

    @Override
    public void evict(Object key) {
        localCache.evict(key);
        super.evict(key);
        pub(new UpdateMessage(key, UpdateMessage.Type.REMOVE));
    }

    @Override
    public void clear() {
        localCache.clear();
        super.clear();
        pub(new UpdateMessage(UpdateMessage.Type.CLEAN));
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = localCache.get(key);
        if (valueWrapper == null) {
            valueWrapper = super.get(key);
            if (valueWrapper != null) {
                localCache.put(key, valueWrapper.get());
                pub(new UpdateMessage(key, valueWrapper.get(), UpdateMessage.Type.PUT));
            }
        }
        return valueWrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T value = localCache.get(key, type);
        if (value == null) {
            value = super.get(key, type);
            if (value != null) {
                localCache.put(key, value);
                pub(new UpdateMessage(key, value, UpdateMessage.Type.PUT));
            }
        }
        return value;
    }

    /**
     * 更新缓存
     *
     * @param updateMessage
     */
    public void sub(final UpdateMessage updateMessage) {

        if (updateMessage.getType() == UpdateMessage.Type.CLEAN) {
            //清除所有缓存
            localCache.clear();
            super.clear();
        } else if (updateMessage.getType() == UpdateMessage.Type.PUT) {
            //更新缓存
            localCache.put(updateMessage.getKey(), updateMessage.getValue());
            super.put(updateMessage.getKey(), updateMessage.getValue());
        } else if (updateMessage.getType() == UpdateMessage.Type.PUTIFABSENT) {
            //更新缓存
            localCache.putIfAbsent(updateMessage.getKey(), updateMessage.getValue());
            super.putIfAbsent(updateMessage.getKey(), updateMessage.getValue());
        } else if (updateMessage.getType() == UpdateMessage.Type.REMOVE) {
            //删除缓存
            localCache.evict(updateMessage.getKey());
            super.evict(updateMessage.getKey());
        }
    }

    /**
     * 通知其他 redis clent 更新缓存
     *
     * @param message
     */
    private void pub(final UpdateMessage message) {
        this.redisOperations.convertAndSend(topicName, message);
    }

}
