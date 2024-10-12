package com.sparta.twingkling001.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    // Value operations
    public void setValues(String key, Object data) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(data);
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, json);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    @Transactional(readOnly = true)
    public <T> T getValues(String key, Class<T> valueType) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String json = values.get(key);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON", e);
            return null;
        }
    }

    // Set operations
    public void addSetValue(String key, String value) {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        setOps.add(key, value);
    }

    public Set<String> getSetValues(String key) {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        return setOps.members(key);
    }

    public void removeSetValue(String key, String value) {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        setOps.remove(key, value);
    }

    // Hash operations
    public void setHashValue(String key, String hashKey, Object value) throws JsonProcessingException {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.put(key, hashKey, objectMapper.writeValueAsString(value));
    }

    public <T> T getHashValue(String key, String hashKey, Class<T> valueType) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String json = hashOps.get(key, hashKey);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON from hash", e);
            return null;
        }
    }

    public Map<String, String> getHashValues(String key) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.entries(key);
    }

    // Delete operations
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    // Expiration operations
    public Boolean setExpire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    // Check if key exists
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    // 큐에 메시지 추가
    public void addToQueue(String queueName, Object data) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(data);
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        listOps.rightPush(queueName, json);
    }

    // 큐에서 메시지 가져오기
    public <T> T popFromQueue(String queueName, Class<T> valueType) throws JsonProcessingException {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        String json = listOps.leftPop(queueName);
        if (json == null) {
            return null;
        }
        return objectMapper.readValue(json, valueType);
    }

    // 큐의 길이 확인
    public Long getQueueSize(String queueName) {
        return redisTemplate.opsForList().size(queueName);
    }
}