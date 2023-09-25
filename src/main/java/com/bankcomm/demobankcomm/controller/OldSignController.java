package com.bankcomm.demobankcomm.controller;

import com.bankcomm.demobankcomm.entity.OldSign;
import com.bankcomm.demobankcomm.service.IOldSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/24
 * Time: 21:52
 */
@RestController
@Slf4j
@RequestMapping("/oldSign")
public class OldSignController {
    @Resource
    private IOldSignService oldSignService;

    @GetMapping("/info/{idYearMonth}")
    public List<OldSign> getOldSignList(@PathVariable("idYearMonth") String idYearMonth) {
        return oldSignService.getSignedList(idYearMonth);
    }

    @GetMapping("/info/count/{idYearMonth}")
    // 当月签到天数
    public int getSignedCount(@PathVariable("idYearMonth")String idYearMonth) {
        return oldSignService.signedCount(idYearMonth);
    }

    @GetMapping("/info/max_continue_days/{idYearMonth}")
    // 最大连续签到天数
    public int maxContinue(@PathVariable("idYearMonth")String idYearMonth) {
        return oldSignService.maxContinue(idYearMonth);
    }

    @PostMapping("/sign/{idYearMonthDay}")
    public boolean sign(@PathVariable String idYearMonthDay) {
        return oldSignService.sign(idYearMonthDay);
    }

    @PutMapping("/supplementary/{idYearMonthDay}")
    // 补签
    public boolean supplementary(@PathVariable String idYearMonthDay) {
        return oldSignService.supplementary(idYearMonthDay);
    }

}

