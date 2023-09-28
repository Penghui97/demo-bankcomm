package com.bankcomm.demobankcomm;

import com.bankcomm.demobankcomm.entity.NewSign;
import com.bankcomm.demobankcomm.entity.OldSign;
import com.bankcomm.demobankcomm.service.INewSignService;
import com.bankcomm.demobankcomm.service.IOldSignService;
import com.bankcomm.demobankcomm.utils.ByteConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/27
 * Time: 09:36
 */
@Slf4j
@SpringBootTest
class NewSignTest {
    @Resource
    private INewSignService newSignService;
    @Resource
    private IOldSignService oldSignService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    // 插入假数据
    void addData() {
//        stringRedisTemplate.opsForValue().setBit("test", 2, true);
        Set<Integer> set30 = new HashSet<>();
        set30.add(4);set30.add(6);set30.add(9);set30.add(11);
        String idPrefix = "123:2023-0";
        for (int i = 1; i < 10; i++) {
            String id = idPrefix + i;
            int l;
            if (i == 2) {
                l = 28;
            } else if (set30.contains(i))
                l = 30;
            else
                l = 31;

            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            String key = "123:2023:0" + i;
            for (int j = 0; j < l; j++) {
                String oldId = j < 9 ? id + "-0" + (j + 1) : id + "-" + (j + 1);
                int r = random.nextInt(2);
                stringRedisTemplate.opsForValue().setBit(key, j, r == 1);
                oldSignService.save(new OldSign(oldId, r));
                sb.append(r);
            }
            newSignService.save(new NewSign(id, ByteConvertUtil.string2ByteArray(sb.toString())));
        }

    }

    @Test
    // 插入更多无用数据
    void addUseless() {
        Set<Integer> set30 = new HashSet<>();
        set30.add(4);set30.add(6);set30.add(9);set30.add(11);
        int userId = 100125;
        for (int m = 0; m < 100000; m++) {
            String idPrefix = userId + ":2023-0";
            for (int i = 1; i < 10; i++) {
                String id = idPrefix + i;
                int l;
                if (i == 2) {
                    l = 28;
                } else if (set30.contains(i))
                    l = 30;
                else
                    l = 31;

                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                String key = userId + ":2023:0" + i;
                for (int j = 0; j < l; j++) {
                    String oldId = j < 9 ? id + "-0" + (j + 1) : id + "-" + (j + 1);
                    int r = random.nextInt(2);
                    stringRedisTemplate.opsForValue().setBit(key, j, r == 1);
                    oldSignService.save(new OldSign(oldId, r));
                    sb.append(r);
                }
                newSignService.save(new NewSign(id, ByteConvertUtil.string2ByteArray(sb.toString())));
            }
            userId ++;
        }
    }

    @Test
        // 插入更多无用数据
    void addUseless2() {
        Set<Integer> set30 = new HashSet<>();
        set30.add(4);set30.add(6);set30.add(9);set30.add(11);
        int userId = 200126;
        for (int m = 0; m < 100000; m++) {
            String idPrefix = userId + ":2023-0";
            for (int i = 1; i < 10; i++) {
                String id = idPrefix + i;
                int l;
                if (i == 2) {
                    l = 28;
                } else if (set30.contains(i))
                    l = 30;
                else
                    l = 31;

                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                String key = userId + ":2023:0" + i;
                for (int j = 0; j < l; j++) {
                    String oldId = j < 9 ? id + "-0" + (j + 1) : id + "-" + (j + 1);
                    int r = random.nextInt(2);
                    stringRedisTemplate.opsForValue().setBit(key, j, r == 1);
                    oldSignService.save(new OldSign(oldId, r));
                    sb.append(r);
                }
                newSignService.save(new NewSign(id, ByteConvertUtil.string2ByteArray(sb.toString())));
            }
            userId ++;
        }
    }

    @Test
        // 插入更多无用数据
    void addUseless3() {
        Set<Integer> set30 = new HashSet<>();
        set30.add(4);set30.add(6);set30.add(9);set30.add(11);
        int userId = 300127;
        for (int m = 0; m < 100000; m++) {
            String idPrefix = userId + ":2023-0";
            for (int i = 1; i < 10; i++) {
                String id = idPrefix + i;
                int l;
                if (i == 2) {
                    l = 28;
                } else if (set30.contains(i))
                    l = 30;
                else
                    l = 31;

                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                String key = userId + ":2023:0" + i;
                for (int j = 0; j < l; j++) {
                    String oldId = j < 9 ? id + "-0" + (j + 1) : id + "-" + (j + 1);
                    int r = random.nextInt(2);
                    stringRedisTemplate.opsForValue().setBit(key, j, r == 1);
                    oldSignService.save(new OldSign(oldId, r));
                    sb.append(r);
                }
                newSignService.save(new NewSign(id, ByteConvertUtil.string2ByteArray(sb.toString())));
            }
            userId ++;
        }
    }

    @Test
        // 插入更多无用数据
    void addUseless4() {
        Set<Integer> set30 = new HashSet<>();
        set30.add(4);set30.add(6);set30.add(9);set30.add(11);
        int userId = 400127;
        for (int m = 0; m < 50000; m++) {
            String idPrefix = userId + ":2023-0";
            for (int i = 1; i < 10; i++) {
                String id = idPrefix + i;
                int l;
                if (i == 2) {
                    l = 28;
                } else if (set30.contains(i))
                    l = 30;
                else
                    l = 31;

                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                String key = userId + ":2023:0" + i;
                for (int j = 0; j < l; j++) {
                    String oldId = j < 9 ? id + "-0" + (j + 1) : id + "-" + (j + 1);
                    int r = random.nextInt(2);
                    stringRedisTemplate.opsForValue().setBit(key, j, r == 1);
                    oldSignService.save(new OldSign(oldId, r));
                    sb.append(r);
                }
                newSignService.save(new NewSign(id, ByteConvertUtil.string2ByteArray(sb.toString())));
            }
            userId ++;
        }
    }

    @Test
        // 插入更多无用数据
    void addUseless5() {
        Set<Integer> set30 = new HashSet<>();
        set30.add(4);set30.add(6);set30.add(9);set30.add(11);
        int userId = 450127;
        for (int m = 0; m < 50000; m++) {
            String idPrefix = userId + ":2023-0";
            for (int i = 1; i < 10; i++) {
                String id = idPrefix + i;
                int l;
                if (i == 2) {
                    l = 28;
                } else if (set30.contains(i))
                    l = 30;
                else
                    l = 31;

                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                String key = userId + ":2023:0" + i;
                for (int j = 0; j < l; j++) {
                    String oldId = j < 9 ? id + "-0" + (j + 1) : id + "-" + (j + 1);
                    int r = random.nextInt(2);
                    stringRedisTemplate.opsForValue().setBit(key, j, r == 1);
                    oldSignService.save(new OldSign(oldId, r));
                    sb.append(r);
                }
                newSignService.save(new NewSign(id, ByteConvertUtil.string2ByteArray(sb.toString())));
            }
            userId ++;
        }
    }

    @Test
        // 插入更多无用数据
    void addUseless6() {
        Set<Integer> set30 = new HashSet<>();
        set30.add(4);set30.add(6);set30.add(9);set30.add(11);
        int userId = 500127;
        for (int m = 0; m < 50000; m++) {
            String idPrefix = userId + ":2023-0";
            for (int i = 1; i < 10; i++) {
                String id = idPrefix + i;
                int l;
                if (i == 2) {
                    l = 28;
                } else if (set30.contains(i))
                    l = 30;
                else
                    l = 31;

                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                String key = userId + ":2023:0" + i;
                for (int j = 0; j < l; j++) {
                    String oldId = j < 9 ? id + "-0" + (j + 1) : id + "-" + (j + 1);
                    int r = random.nextInt(2);
                    stringRedisTemplate.opsForValue().setBit(key, j, r == 1);
                    oldSignService.save(new OldSign(oldId, r));
                    sb.append(r);
                }
                newSignService.save(new NewSign(id, ByteConvertUtil.string2ByteArray(sb.toString())));
            }
            userId ++;
        }
    }

    @Test
        // 插入更多无用数据
    void addUseless7() {
        Set<Integer> set30 = new HashSet<>();
        set30.add(4);set30.add(6);set30.add(9);set30.add(11);
        int userId = 550127;
        for (int m = 0; m < 50000; m++) {
            String idPrefix = userId + ":2023-0";
            for (int i = 1; i < 10; i++) {
                String id = idPrefix + i;
                int l;
                if (i == 2) {
                    l = 28;
                } else if (set30.contains(i))
                    l = 30;
                else
                    l = 31;

                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                String key = userId + ":2023:0" + i;
                for (int j = 0; j < l; j++) {
                    String oldId = j < 9 ? id + "-0" + (j + 1) : id + "-" + (j + 1);
                    int r = random.nextInt(2);
                    stringRedisTemplate.opsForValue().setBit(key, j, r == 1);
                    oldSignService.save(new OldSign(oldId, r));
                    sb.append(r);
                }
                newSignService.save(new NewSign(id, ByteConvertUtil.string2ByteArray(sb.toString())));
            }
            userId ++;
        }
    }
    // 测试获取数据
    @Test
    void getTest() {
        List<Long> result = stringRedisTemplate.opsForValue().bitField(
                "123:2023:07",
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(32)).valueAt(0)
        );
        assert result != null;
        Long l = result.get(0);
        log.info("l: " + l);
        NewSign newSign = newSignService.getById("123:2023-07");
        byte[] bytes = newSign.getSigned();
        log.info(Arrays.toString(bytes));
        log.info(ByteConvertUtil.byteArrayToLong(bytes).toString());
        log.info(Arrays.toString(ByteConvertUtil.longToByteArray(l)));

    }

}

