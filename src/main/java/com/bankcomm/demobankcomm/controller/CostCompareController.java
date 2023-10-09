package com.bankcomm.demobankcomm.controller;

import com.bankcomm.demobankcomm.service.INewSignService;
import com.bankcomm.demobankcomm.service.IOldSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: binghujuelie
 * Date: 2023/10/9
 * Time: 13:53
 */
@RestController
@Slf4j
@RequestMapping("/costCompare")
public class CostCompareController {
    @Resource
    private IOldSignService oldSignService;
    @Resource
    private INewSignService newSignService;

    // 比较获取签到列表，n代表数据量大小，大于等于1的正整数
    @GetMapping("/getSignInfo/{n}")
    public Map<String, Object> getSignedCount(@PathVariable int n) {
        Random random = new Random();
        int totalOld = 0;
        int totalNew = 0;
        int totalDifference = 0;
        ArrayList<String> keyList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 初始化计时器
            StopWatch oldWatch = new StopWatch();
            StopWatch newWatch = new StopWatch();
            // 随机生成查询参数字符串，并保存到 keyList
            String str = "t" + random.nextInt(350000) + ":" + "2023-0" + (random.nextInt(9) + 1);
            keyList.add(str);
            // 先各跑一遍，去除连接数据库耗时影响
            oldSignService.getSignedList(str);
            newSignService.getNewSignDTO(str);
            // 老签到计时
            oldWatch.start();
            oldSignService.getSignedList(str);
            oldWatch.stop();
            long oldSign = oldWatch.getTotalTimeMillis();
            // 新签到计时
            newWatch.start();
            newSignService.getNewSignDTO(str);
            newWatch.stop();
            long newSign = newWatch.getTotalTimeMillis();
            // 计算差值
            totalOld += oldSign;
            totalNew += newSign;
        }
        Map<String, Object> result = new HashMap<>();
        totalDifference = totalOld - totalNew;
        // 老签到总耗时
        result.put("old_sign_total", totalOld);
        // 新签到总耗时
        result.put("new_sign_total", totalNew);
        // 新老签到总耗时差
        result.put("difference_total", totalDifference);
        // 性能提升比率
        double rate = (double) totalDifference / totalOld;
        result.put("optimized_rate", new DecimalFormat("0.00").format(rate * 100) + "%");
        // 请求参数列表
        result.put("key_list", keyList);
        return result;
    }
}
