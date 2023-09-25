package com.bankcomm.demobankcomm.service;

import com.bankcomm.demobankcomm.entity.OldSign;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.*;

public interface IOldSignService extends IService<OldSign> {
    List<OldSign> getSignedList(String idYearMonth);
    int signedCount(String idYearMonth);

    boolean sign(String idYearMonthDay);

    boolean supplementary(String idYearMonthDay);

    int maxContinue(String idYearMonth);
}
