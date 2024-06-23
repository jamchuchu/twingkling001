package com.sparta.twingkling001.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test/redis")
public class RedisTest {
    private final RedisService redisService;

    @GetMapping("/set")
    public void setRedisInfo(){
        redisService.setValues("testNum", "81");
    }

    @GetMapping("/get")
    public void getRedisInfo(){
        String response = redisService.getValues("testNum");
        log.info("testNum결과"  +  response);
    }
}
