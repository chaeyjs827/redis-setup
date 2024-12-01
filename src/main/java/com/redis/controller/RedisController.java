package com.redis.controller;

import com.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    private final RedisService redisService;

    @Autowired
    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/set")
    public String setValue(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
        redisService.setValue(key, value);
        return "캐시 생성 완료!!";
    }

    @GetMapping("/get")
    public String getValue(@RequestParam(value = "key") String key) {
        return redisService.getValue(key);
    }
}