package com.bankcomm.demobankcomm;

import com.bankcomm.demobankcomm.entity.NewSign;
import com.bankcomm.demobankcomm.entity.OldSign;
import com.bankcomm.demobankcomm.service.INewSignService;
import com.bankcomm.demobankcomm.service.IOldSignService;
import com.bankcomm.demobankcomm.utils.ByteConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
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
    @Transactional
    @Rollback(value = false)
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
            newSignService.save(new NewSign(id, ByteConvertUtil.string2Byte(sb.toString())));
        }

    }


}

