package com.bankcomm.demobankcomm.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/22
 * Time: 10:20
 */

@Slf4j
@RestController
public class testController {
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 测试用
    @GetMapping("/test")
    public String test() {
        return "hello world";
    }

    @GetMapping("/initialize")
    public String initialize() {
        stringRedisTemplate.opsForValue().set("remain", "1500");
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("userSet")))
            stringRedisTemplate.delete("userSet");
        return "初始化成功";
    }

    @GetMapping("/testLock/{userId}")
    public String testLock(@PathVariable String userId) {
        RLock lock = redissonClient.getLock("lock");
        boolean isLock;
        try {
            isLock = lock.tryLock(30L, 30L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (isLock) {
            try {
                // 查看是否重复下单
                if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember("userSet", userId))) {
                    return "不允许重复下单" + userId;
                }
                // 查看剩余票量是否充足
                if (Objects.equals(stringRedisTemplate.opsForValue().get("remain"), "0")) {
                    return "售罄" + userId;
                }
                // 扣减一张票
                stringRedisTemplate.opsForValue().decrement("remain");
                // 加入集合
                stringRedisTemplate.opsForSet().add("userSet", userId);
                return "购票成功" + userId;
            } finally {
                lock.unlock();
            }

        }
        return "请重试" + userId;
    }
}

