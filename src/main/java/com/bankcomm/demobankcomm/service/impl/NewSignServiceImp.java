package com.bankcomm.demobankcomm.service.impl;

import com.bankcomm.demobankcomm.dto.NewSignDTO;
import com.bankcomm.demobankcomm.entity.NewSign;
import com.bankcomm.demobankcomm.mapper.NewSignMapper;
import com.bankcomm.demobankcomm.service.INewSignService;
import com.bankcomm.demobankcomm.utils.ByteConvertUtil;
import com.bankcomm.demobankcomm.utils.NewSignConvert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/27
 * Time: 09:33
 */
@Service
public class NewSignServiceImp extends ServiceImpl<NewSignMapper, NewSign> implements INewSignService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
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
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                stringRedisTemplate.opsForValue().setBit(key, i, c == '1');
            }
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
}

