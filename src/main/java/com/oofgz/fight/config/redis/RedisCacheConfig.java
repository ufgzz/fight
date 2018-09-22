package com.oofgz.fight.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oofgz.fight.dto.redis.RedisUser;
import com.oofgz.fight.manager.LocalRedisCache;
import com.oofgz.fight.manager.LocalRedisCacheManager;
import com.oofgz.fight.manager.UpdateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Value("${spring.ext.cache.name:countries}")
    private String localCacheName;

    @Value("${spring.ext.cache.redis.topic:cache}")
    private String topicName;

    @Bean
    public CacheManager jCacheCacheManager() {
        return new JCacheCacheManager(jCacheManagerFactoryBean().getObject());
    }

    @Bean
    public JCacheManagerFactoryBean jCacheManagerFactoryBean() {
        JCacheManagerFactoryBean jCacheManagerFactoryBean = new JCacheManagerFactoryBean();
        Resource resource = new ClassPathResource("ehcache3.xml");
        try {
            jCacheManagerFactoryBean.setCacheManagerUri(resource.getURI());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jCacheManagerFactoryBean;
    }



    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, RedisUser> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, RedisUser> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //template.setValueSerializer(new RedisObjectSerializer());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;

    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        //template.setValueSerializer(new RedisObjectSerializer());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        stringRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        stringRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return stringRedisTemplate;
    }


    @Primary
    @Bean
    public RedisCacheManager redisCacheManager(JedisConnectionFactory jedisConnectionFactory,
                                               RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = LocalRedisCacheManager.create(jedisConnectionFactory,
                jCacheCacheManager().getCache(localCacheName), redisTemplate, topicName);
        return cacheManager;
    }


    @Bean
    public RedisMessageListenerContainer container(JedisConnectionFactory jedisConnectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(topicName));

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(LocalRedisCacheManager localRedisCacheManager, RedisTemplate redisTemplate) {
        return new MessageListenerAdapter(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                byte[] channel = message.getChannel();
                byte[] body = message.getBody();

                String cacheName = (String) redisTemplate.getStringSerializer().deserialize(channel);
                LocalRedisCache cache = (LocalRedisCache) localRedisCacheManager.getCache(cacheName);
                if (cache == null) {
                    return;
                }

                UpdateMessage updateMessage = (UpdateMessage) redisTemplate.getValueSerializer().deserialize(body);
                cache.sub( updateMessage);
            }
        });
    }


}
