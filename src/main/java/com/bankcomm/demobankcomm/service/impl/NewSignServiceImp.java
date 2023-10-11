package com.bankcomm.demobankcomm.service.impl;

import cn.hutool.json.JSONUtil;
import com.bankcomm.demobankcomm.dto.NewSignDTO;
import com.bankcomm.demobankcomm.entity.NewSign;
import com.bankcomm.demobankcomm.mapper.NewSignMapper;
import com.bankcomm.demobankcomm.service.INewSignService;
import com.bankcomm.demobankcomm.utils.ByteConvertUtil;
import com.bankcomm.demobankcomm.utils.NewSignConvert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/27
 * Time: 09:33
 */
@Service
public class NewSignServiceImp extends ServiceImpl<NewSignMapper, NewSign> implements INewSignService {
    private static final ExecutorService SIGN_EXECUTOR = Executors.newFixedThreadPool(2);
    @PostConstruct
    private void init() {
        SIGN_EXECUTOR.submit(new SignHandler());
        SIGN_EXECUTOR.submit(new GetSignHandler());
    }
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Data
    @AllArgsConstructor
    private static class GetSignData {
        private String key;
        private String signed;
    }

    private final BlockingQueue<GetSignData> getSignDataTasks = new LinkedBlockingQueue<>();
    // 插入redis的任务
    private class GetSignHandler implements Runnable {

        @Override
        public void run() {
            for (;;) {
                try {
                    GetSignData getSignData = getSignDataTasks.take();
                    String s = getSignData.getSigned();
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        stringRedisTemplate.opsForValue().setBit(getSignData.getKey(), i, c == '1');
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
    public NewSignDTO getNewSignDTO(String idYearMonth) {
        String key = idYearMonth.replace('-', ':');
        List<Long> result = stringRedisTemplate.opsForValue().bitField(
                key,
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(32)).valueAt(0)
        );
        // 如果redis中没有数据，从数据库获取并储存到redis
        if (result == null || result.isEmpty()) {
            NewSign newSign = getById(idYearMonth);
            String s = ByteConvertUtil.byteArray2BinaryString(newSign.getSigned());
            // 异步插入redis
            getSignDataTasks.add(new GetSignData(key, s));
            return new NewSignDTO(newSign.getId(), s);
        }
        // 如果redis中有数据，直接返回
        Long num = result.get(0);
        byte[] bytes = ByteConvertUtil.longToByteArray(num);
        return NewSignConvert.toFrontEnd(new NewSign(idYearMonth, bytes));
    }

    @Override
    public int getSignedCount(String idYearMonth) {
        byte[] signed = getById(idYearMonth).getSigned();
        return ByteConvertUtil.countOnesInByteArray(signed);
    }

    @Override
    public int maxContinue(String idYearMonth) {
        byte[] signed = getById(idYearMonth).getSigned();
        return ByteConvertUtil.maxContinue(signed);
    }

    @Data
    @AllArgsConstructor
    private static class SignData {
        private String key;
        private int day;
        private NewSign newSign;
    }
    // 用阻塞队列模拟消息队列异步更新
    private final BlockingQueue<SignData> signTasks = new LinkedBlockingQueue<>();
    // 异步更新签到数据库任务
    private class SignHandler implements Runnable {
        @Override
        public void run() {
            for (;;) {
                NewSign newSign;
                int day;
                String key;
                try {
                    SignData signData = signTasks.take();
                    newSign = signData.getNewSign();
                    day = signData.getDay();
                    key = signData.getKey();
                    // 更新redis
                    stringRedisTemplate.opsForValue().setBit(key, day - 1, true);
                    // 更新签到数据库
                    if (getById(newSign.getId()) == null)
                        save(newSign);
                    else
                        updateById(newSign);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Boolean sign(String json) {
        Map<String, String> map = JSONUtil.toBean(json, Map.class);
        String key = map.get("key");
        int day = Integer.parseInt(map.get("day"));
        String id = map.get("id");
        String signed = map.get("signed");

        // 异步更新redis和数据库
        NewSign newSign = new NewSign(id, ByteConvertUtil.string2ByteArray(signed));
        return signTasks.add(new SignData(key, day, newSign));
    }

    // 补签
    @Override
    public Boolean supplementary(String json) {
        return sign(json);
    }
}

