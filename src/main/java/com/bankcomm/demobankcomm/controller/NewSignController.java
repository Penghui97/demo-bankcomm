package com.bankcomm.demobankcomm.controller;

import cn.hutool.json.JSONUtil;
import com.bankcomm.demobankcomm.dto.NewSignDTO;
import com.bankcomm.demobankcomm.service.INewSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/27
 * Time: 16:27
 */
@RestController
@Slf4j
@RequestMapping("/newSign")
@CrossOrigin
public class NewSignController {

    @Resource
    private INewSignService newSignService;


    // 获取当月签到
    @GetMapping("/info/{idYearMonth}")
    public NewSignDTO getNewSignDTO(@PathVariable String idYearMonth) {
        return newSignService.getNewSignDTO(idYearMonth);
    }

    // 当月签到天数
    @GetMapping("/info/count/{idYearMonth}")
    public int getSignedCount(@PathVariable String idYearMonth) {
        return newSignService.getSignedCount(idYearMonth);
    }

    // 最大连续签到
    @GetMapping("/info/max_continue_days/{idYearMonth}")
    public int maxContinue(@PathVariable String idYearMonth) {
        return newSignService.maxContinue(idYearMonth);
    }

    // 签到
    @PostMapping("/sign")
    public Boolean sign(@RequestBody String json) {
        return newSignService.sign(json);
    }

    // 补签
    @PutMapping("/supplementary")
    public Boolean supplementary(@RequestBody String json) {
        return newSignService.supplementary(json);
    }

}

