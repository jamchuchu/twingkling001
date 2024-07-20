package com.sparta.twingkling001.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sparta.twingkling001.product.dto.response.ProductRespDto;
import com.sparta.twingkling001.product.entity.ProductDetail;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    private final RedisProperties redisProperties;


    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "order-lock");
    }

    // RedisProperties로 yaml에 저장한 host, post를 연결
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Key serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Value serializer
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        return redisTemplate;
    }
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
//                .entryTtl(Duration.ofMinutes(3L));
//
//        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .build();
//    }


    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .entryTtl(Duration.ofMinutes(3L));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
        }

//    /**
//     * Redis Cache 설정
//     * Map을 이용하여 캐시 키를 등록
//     * 모든 키에 대한 설정이 아닌 각각의 키 별로 지정할 수 있게 STUDYGROUP_LIST 키 하나에 한정해서 180초로 설정
//     */
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext
//                        .SerializationPair
//                        .fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext
//                        .SerializationPair
//                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
//
//        // entryTtl 설정
//        cacheConfigurationMap.put(RedisCacheKey.STUDYGROUP_LIST,
//                redisCacheConfiguration.entryTtl(Duration.ofSeconds(180L)));
//
//        return RedisCacheManager
//                .RedisCacheManagerBuilder
//                .fromConnectionFactory(redisConnectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .build();
//    }

//    @Bean
//    public CacheManager cacheManager() {
//        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
//                .allowIfBaseType(Object.class)  // 모든 클래스 허용
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.activateDefaultTyping(ptv,ObjectMapper.DefaultTyping.NON_FINAL);
//
//        //localDateTime 역직렬화 세팅
//        objectMapper.registerModule(new JavaTimeModule());
//        //
//
//        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofDays(5))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
//
//        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();
//        configurations.put(ConstantPool.CacheName.POST, defaultConfig.entryTtl(Duration.ofDays(1)));
//        configurations.put(ConstantPool.CacheName.VIEWS, defaultConfig.entryTtl(Duration.ofDays(1)));
//
//        return RedisCacheManager.RedisCacheManagerBuilder
//                .fromConnectionFactory(redisConnectionFactory())
//                .cacheDefaults(defaultConfig)
//                .withInitialCacheConfigurations(configurations)
//                .build();
//    }
}