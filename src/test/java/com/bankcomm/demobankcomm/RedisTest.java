package com.bankcomm.demobankcomm;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoLocation;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2024/7/14
 * Time: 14:32
 */

@Slf4j
@SpringBootTest
class RedisTest {
    @Resource
    private RedissonClient redissonClient;

    private RLock lock;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setUp() {
        lock = redissonClient.getLock("lock");
    }

    @Test
    void lockTest() throws InterruptedException {
        Thread t = new Thread(() -> {
            // 尝试获取锁
            boolean isLock = false;
            try {
                isLock = lock.tryLock(1L, 10L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!isLock) {
                log.error("获取锁失败 .... 1");
                return;
            }
            try {
                log.info("获取锁成功 .... 1");
                log.info("开始执行业务 ... 1");
            } finally {
                log.warn("准备释放锁 .... 1");
                lock.unlock();
            }
        });

    }
    void method() throws InterruptedException {
        // 尝试获取锁
        boolean isLock = lock.tryLock(1L,2L, TimeUnit.SECONDS);
        if (!isLock) {
            log.error("获取锁失败 .... 2");
            return;
        }
        try {
            log.info("获取锁成功 .... 2");
            log.info("开始执行业务 ... 2");
        } finally {
            log.warn("准备释放锁 .... 2");
            lock.unlock();
        }
    }

    @Test
    void test() {
        stringRedisTemplate.opsForValue().set("name", "l");
    }

    @Test
    void geoTest() {
        Set<String> coordinate = stringRedisTemplate.opsForZSet().range("coordinate", 0, -1);
        assert coordinate != null;
        List<GeoLocation<String>> list = new ArrayList<>();
        for (String member : coordinate) {
            list.add(new GeoLocation<>(member,
                    Objects.requireNonNull(stringRedisTemplate.opsForGeo().position("coordinate", member)).get(0)));
        }
        log.info(list.toString());
    }
}

