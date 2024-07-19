package com.bankcomm.demobankcomm.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.util.*;
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
        String remain = "1500";
        stringRedisTemplate.opsForValue().set("remain", remain);
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("userSet")))
            stringRedisTemplate.delete("userSet");
        log.info("初始化成功，剩余票数" + remain);
        return "初始化成功，剩余票数" + remain;
    }


    // 测试分布式锁
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
                    log.error("不允许重复下单" + userId);
                    return "不允许重复下单" + userId;
                }
                // 查看剩余票量是否充足
                if (Objects.equals(stringRedisTemplate.opsForValue().get("remain"), "0")) {
                    log.error("售罄" + userId);
                    return "售罄" + userId;
                }
                // 扣减一张票
                stringRedisTemplate.opsForValue().decrement("remain");
                // 加入集合
                stringRedisTemplate.opsForSet().add("userSet", userId);
                log.info("购票成功" + userId);
                return "购票成功" + userId;
            } finally {
                lock.unlock();
            }

        }
        log.error("请重试" + userId);
        return "请重试" + userId;
    }

    // 测试Lua脚本
    @GetMapping("/testLua/{userId}")
    public String testLua(@PathVariable String userId) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("script.lua"));
        redisScript.setResultType(String.class);
        List<String> keys = Arrays.asList(userId, "userSet", "remain");
        String r = stringRedisTemplate.execute(redisScript, keys);
        if (!Objects.requireNonNull(r).contains("成功")) {
            log.error(r);
        } else {
            log.info(r);
        }
        return r;
    }

    // 测试事务
    @GetMapping("/testTransaction/{userId}")
    public String testTransaction(@PathVariable String userId) {
        List<Object> results = stringRedisTemplate.execute(new SessionCallback<>() {

            @Override
            public List<Object> execute(@NonNull RedisOperations operations) throws DataAccessException {
                operations.watch("userSet"); // 监视键
                operations.watch("remain"); // 监视键
                // 预检查逻辑，事务之外
                Boolean isMember = operations.opsForSet().isMember("userSet", userId);
                String remain = (String) operations.opsForValue().get("remain");

                if (Boolean.TRUE.equals(isMember)) {
                    return Collections.singletonList("不允许重复下单" + userId);
                }
                if ("0".equals(remain)) {
                    return Collections.singletonList("售罄" + userId);
                }

                // 开启事务
                operations.multi();
                operations.opsForValue().decrement("remain");
                operations.opsForSet().add("userSet", userId);
                return operations.exec(); // 提交事务
            }
        });
        // 根据事务执行结果进行响应
        if (results == null || results.isEmpty()) {
            return "请重试" + userId;
        }
        if (results.size() == 1 && results.get(0) instanceof String) {
            return (String) results.get(0);
        }
        return "购票成功" + userId;
    }

    @GetMapping("/testTransactionRetry/{userId}")
    public String testTransactionRetry(@PathVariable String userId) {
        int retryCount = 5; // 重试次数
        while (retryCount-- > 0) {
            // 使用SessionCallback来执行Redis事务
            List<Object> results = stringRedisTemplate.execute(new SessionCallback<>() {
                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    operations.watch("userSet"); // 监视键
                    operations.watch("remain"); // 监视键

                    // 预检查逻辑，事务之外
                    Boolean isMember = operations.opsForSet().isMember("userSet", userId);
                    String remain = (String) operations.opsForValue().get("remain");

                    if (Boolean.TRUE.equals(isMember)) {
                        return Collections.singletonList("不允许重复下单" + userId);
                    }
                    if ("0".equals(remain)) {
                        return Collections.singletonList("售罄" + userId);
                    }

                    // 开启事务
                    operations.multi();
                    operations.opsForValue().decrement("remain");
                    operations.opsForSet().add("userSet", userId);
                    return operations.exec(); // 提交事务
                }
            });

            // 根据事务执行结果进行响应
            if (results != null && !results.isEmpty()) {
                if (results.size() == 1 && results.get(0) instanceof String) {
                    return (String) results.get(0);
                }
                return "购票成功" + userId;
            }
        }
        return "请重试" + userId;
    }


}

