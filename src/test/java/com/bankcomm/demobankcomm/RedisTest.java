package com.bankcomm.demobankcomm;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Qualifier("remoteStringRedisTemplate")
    private StringRedisTemplate remoteRedisTemplate;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    void printRedisConnectionInfo() {
        LettuceConnectionFactory lettuce = (LettuceConnectionFactory) redisConnectionFactory;
        System.out.println("🔌 Redis Host: " + lettuce.getHostName());
        System.out.println("🔌 Redis Port: " + lettuce.getPort());
        System.out.println("🧠 Redis DB: " + lettuce.getDatabase());
        System.out.println("🔑 Redis Password: " + lettuce.getPassword());
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

    @Test
    void geoTest1() {
        String key = "coordinate";
        // 两点间距离
        Distance distance1 = stringRedisTemplate.opsForGeo().
                distance(key, "通泰大厦", "肖鹏辉家", Metrics.KILOMETERS);
        Distance distance2 = stringRedisTemplate.opsForGeo().
                distance(key, "通泰大厦", "北京图书大厦"); // 默认单位是米
        log.info("通泰大厦和肖鹏辉家的距离为" + Objects.requireNonNull(distance1));
        log.info("通泰大厦和北京图书大厦的距离为" + Objects.requireNonNull(distance2));
    }

    @Test
    void geoTest2() {
        String key = "coordinate";
        // 获取通泰大厦周围方圆3km的所有地点
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults =
                stringRedisTemplate.opsForGeo().radius(key, "通泰大厦", 3000);
        log.info(String.valueOf(geoResults));
    }

    @Test
    void geoTest3() {
        String key = "coordinate";
        // 获取通泰大厦周围方圆30km的前9个从远到近的地点并携带距离和坐标信息
        RedisGeoCommands.GeoRadiusCommandArgs commandArgs = RedisGeoCommands.GeoRadiusCommandArgs.
                newGeoRadiusArgs().includeDistance().
                includeCoordinates().sortDescending().limit(9);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = stringRedisTemplate.opsForGeo().
                radius(key, "通泰大厦", new Distance(30, Metrics.KILOMETERS), commandArgs);
        log.info(String.valueOf(geoResults));
    }

    @Test
    void geoTest4() {
        String key = "coordinate";
        // 获取通泰大厦周围方圆30km的所有地点并携带距离和坐标信息，从近到远排序
        RedisGeoCommands.GeoRadiusCommandArgs commandArgs = RedisGeoCommands.GeoRadiusCommandArgs.
                newGeoRadiusArgs().includeDistance().
                includeCoordinates().sortAscending();
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = stringRedisTemplate.opsForGeo().
                radius(key, "通泰大厦", new Distance(30, Metrics.KILOMETERS), commandArgs);
        log.info(String.valueOf(geoResults));
    }

    @Test
    void doubleWrite() {
        String key = "number";
        String value = "10";

        String lockKey = "lock:write:" + key;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 获取锁（最多等3秒，锁保持10秒自动释放）
            if (lock.tryLock(3, 10, TimeUnit.SECONDS)) {
                // 写本地 Redis
                stringRedisTemplate.opsForValue().set(key, value);
                // 写远程 Redis
                remoteRedisTemplate.opsForValue().set(key, value);
            } else {
                log.info("获取锁失败，跳过双写操作");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("加锁时被中断");
        } finally {
            // 释放锁（先判断再释放，防止误解锁）
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Test
    void testTime() {
        String key = "number";
        String value = "10";
        long start = System.currentTimeMillis();

        // 本地写入
        stringRedisTemplate.opsForValue().set(key, value);
        long localWriteEnd = System.currentTimeMillis();

        // 远程写入
        remoteRedisTemplate.opsForValue().set(key, value);
        long remoteWriteEnd = System.currentTimeMillis();

        log.info("✅ 本地 Redis 写入耗时: " + (localWriteEnd - start) + " ms");
        log.info("✅ 远程 Redis 写入耗时: " + (remoteWriteEnd - localWriteEnd) + " ms");
    }

}

