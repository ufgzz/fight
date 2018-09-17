package com.oofgz.fight.listener;

import com.oofgz.fight.dto.redis.CacheMessage;
import com.oofgz.fight.manager.RedisEhcacheCacheManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 监听redis消息需要实现MessageListener接口
 */
@Log4j2
public class CacheMessageListener implements MessageListener {

    private RedisTemplate<Object, Object> redisTemplate;

    private RedisEhcacheCacheManager redisEhcacheCacheManager;

    public CacheMessageListener(RedisTemplate<Object, Object> redisTemplate,
                                RedisEhcacheCacheManager redisEhcacheCacheManager) {
        super();
        this.redisTemplate = redisTemplate;
        this.redisEhcacheCacheManager = redisEhcacheCacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.debug("receive a redis topic message, clear local cache, the cacheName is {}, the key is {}", cacheMessage.getCacheName(), cacheMessage.getKey());
        redisEhcacheCacheManager.clearLocal(
                cacheMessage.getCacheName(),
                cacheMessage.getKey(),
                cacheMessage.getSender()
        );
    }

}
