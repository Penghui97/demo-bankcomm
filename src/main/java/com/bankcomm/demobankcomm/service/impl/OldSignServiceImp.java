package com.bankcomm.demobankcomm.service.impl;

import com.bankcomm.demobankcomm.entity.OldSign;
import com.bankcomm.demobankcomm.mapper.OldSignMapper;
import com.bankcomm.demobankcomm.service.IOldSignService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/24
 * Time: 15:00
 */
@Service
public class OldSignServiceImp extends ServiceImpl<OldSignMapper, OldSign> implements IOldSignService {
    @Override
    public List<OldSign> getSignedList(String idYearMonth) {
        QueryWrapper<OldSign> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("id", idYearMonth + "%");
        return list(queryWrapper);
    }

    @Override
    public int signedCount(String idYearMonth) {
        return query().like("id", idYearMonth + "%")
                        .eq("signed", 1).count();
    }

    @Override
    public boolean sign(String idYearMonthDay) {
        OldSign oldSign = new OldSign(idYearMonthDay, 1);
        return save(oldSign);
    }

    @Override
    public boolean supplementary(String idYearMonthDay) {
        return updateById(new OldSign(idYearMonthDay, 1));
    }

    @Override
    public int maxContinue(String idYearMonth) {
        List<OldSign> signedList = getSignedList(idYearMonth);
        int max = 0;
        int n = signedList.size();
        int l = 0;
        int r = 1;
        while (l < n && r < n) {
            int signed = signedList.get(l).getSigned();
            int count = 0;
            if (signed == 1) {
                count ++;
                r = l + 1;
                while (r < n && signedList.get(r).getSigned() == 1) {
                    count ++;
                    r ++;
                }
                max = Math.max(max, count);
                l = r + 1;
            } else
                l ++;
        }
        return max;
    }
}

