package com.bankcomm.demobankcomm.service;

import com.bankcomm.demobankcomm.dto.NewSignDTO;
import com.bankcomm.demobankcomm.entity.NewSign;
import com.baomidou.mybatisplus.extension.service.IService;

public interface INewSignService extends IService<NewSign> {
    NewSignDTO getNewSignDTO(String idYearMonth);
    int getSignedCount(String idYearMonth);

    int maxContinue(String idYearMonth);

    Boolean sign(String idYearMonth);

    Boolean supplementary(String json);
}
