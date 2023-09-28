package com.bankcomm.demobankcomm.controller;

import com.bankcomm.demobankcomm.dto.NewSignDTO;
import com.bankcomm.demobankcomm.entity.NewSign;
import com.bankcomm.demobankcomm.service.INewSignService;
import com.bankcomm.demobankcomm.utils.ByteConvertUtil;
import com.bankcomm.demobankcomm.utils.NewSignConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/27
 * Time: 16:27
 */
@RestController
@Slf4j
@RequestMapping("/newSign")
public class NewSignController {

    @Resource
    private INewSignService newSignService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/info/{idYearMonth}")
    public NewSignDTO getNewSignDTO(@PathVariable("idYearMonth") String idYearMonth) {
        String key = idYearMonth.replace('-', ':');
        List<Long> result = stringRedisTemplate.opsForValue().bitField(
                key,
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(32)).valueAt(0)
        );
        // 如果redis中没有数据，从数据库获取并储存到redis
        if (result == null || result.isEmpty()) {
            NewSign newSign = newSignService.getById(idYearMonth);
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


}

