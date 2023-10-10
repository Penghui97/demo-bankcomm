package com.bankcomm.demobankcomm.controller;

import cn.hutool.json.JSONUtil;
import com.bankcomm.demobankcomm.entity.OldSign;
import com.bankcomm.demobankcomm.mapper.OldSignMapper;
import com.bankcomm.demobankcomm.service.INewSignService;
import com.bankcomm.demobankcomm.service.IOldSignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

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
public class CostCompareController extends ServiceImpl<OldSignMapper, OldSign> {
    @Resource
    private IOldSignService oldSignService;
    @Resource
    private INewSignService newSignService;

    // 比较获取签到列表，n代表数据量大小，大于等于1的正整数
    @GetMapping("/getSignList/{n}")
    public Map<String, Object> getSignList(@PathVariable int n) {
        Random random = new Random();
        int totalOld = 0;
        int totalNew = 0;
        int totalDifference = 0;
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 初始化计时器
            StopWatch oldWatch = new StopWatch();
            StopWatch newWatch = new StopWatch();
            // 随机生成查询参数字符串，并保存到 idList
            String str = "t" + random.nextInt(350000) + ":" + "2023-0" + (random.nextInt(9) + 1);
            idList.add(str);
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
        result.put("id_list", idList);
        return result;
    }

    // 比较获取签到次数，n代表数据量大小，大于等于1的正整数
    @GetMapping("/getSignedCount/{n}")
    public  Map<String, Object> getSignedCount(@PathVariable int n){
        Random random = new Random();
        int totalOld = 0;
        int totalNew = 0;
        int totalDifference = 0;
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 初始化计时器
            StopWatch oldWatch = new StopWatch();
            StopWatch newWatch = new StopWatch();
            // 随机生成查询参数字符串，并保存到 idList
            String str = "t" + random.nextInt(350000) + ":" + "2023-0" + (random.nextInt(9) + 1);
            idList.add(str);
            // 先各跑一遍，去除连接数据库耗时影响
            oldSignService.signedCount(str);
            newSignService.getSignedCount(str);
            // 老签到计时
            oldWatch.start();
            oldSignService.getSignedList(str);
            oldWatch.stop();
            long oldSign = oldWatch.getTotalTimeMillis();
            // 新签到计时
            newWatch.start();
            newSignService.getSignedCount(str);
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
        result.put("id_list", idList);
        return result;
    }

    // 比较获取最大连续签到次数，n代表数据量大小，大于等于1的正整数
    @GetMapping("/maxContinue/{n}")
    public Map<String, Object> maxContinue(@PathVariable int n){
        Random random = new Random();
        int totalOld = 0;
        int totalNew = 0;
        int totalDifference = 0;
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 初始化计时器
            StopWatch oldWatch = new StopWatch();
            StopWatch newWatch = new StopWatch();
            // 随机生成查询参数字符串，并保存到 idList
            String str = "t" + random.nextInt(350000) + ":" + "2023-0" + (random.nextInt(9) + 1);
            idList.add(str);
            // 先各跑一遍，去除连接数据库耗时影响
            oldSignService.maxContinue(str);
            newSignService.maxContinue(str);
            // 老签到计时
            oldWatch.start();
            oldSignService.maxContinue(str);
            oldWatch.stop();
            long oldSign = oldWatch.getTotalTimeMillis();
            // 新签到计时
            newWatch.start();
            newSignService.maxContinue(str);
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
        result.put("id_list", idList);
        return result;
    }

    // 比较签到功能，n代表数据量大小，大于等于1的正整数
    @GetMapping("/sign/{n}")
    public  Map<String, Object> sign(@PathVariable int n) {
        Random random = new Random();
        int totalOld = 0;
        double totalNew = 0;
        double totalDifference = 0;
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 初始化计时器
            StopWatch oldWatch = new StopWatch();
            StopWatch newWatch = new StopWatch();
            // 随机生成数据，并保存信息到 idList
            int userId=random.nextInt(350000);
            int month=random.nextInt(9) + 1;
            int day=random.nextInt(29) + 1;
            String id="t"+userId+":2024-0"+month;
            idList.add(id);
            String key="t"+userId+":2024:0"+month;
            StringBuilder signed = new StringBuilder();
            for (int i1 = 0; i1 < 32; i1++) {
                signed.append(random.nextInt(1));
            }
            // 新签到请求体
            Map<String, Object> json = new HashMap<>();
            json.put("id", id);
            json.put("key", key);
            json.put("day", day+"");
            json.put("signed", signed);
            String jsonStr=JSONUtil.parse(json).toString();
            // 老签到参数
            String str=id+"-"+day;
            // 先各跑一遍，去除连接数据库耗时影响
            try{
                // 删除已有数据
                removeById(str);
                oldSignService.sign(str);
                // 重新删除已有数据
                removeById(str);
                // 删除重复数据
                signed.setCharAt(day-1, '0');
                json.put("signed", signed.toString());
                jsonStr=JSONUtil.parse(json).toString();
                newSignService.sign(jsonStr);
                // 重新删除已有数据
                signed.setCharAt(day-1, '0');
                json.put("signed", signed.toString());
                jsonStr=JSONUtil.parse(json).toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            // 老签到计时
            oldWatch.start();
            oldSignService.sign(str);
            oldWatch.stop();
            long oldSign = oldWatch.getTotalTimeMillis();
            // 新签到计时
            newWatch.start();
            newSignService.sign(jsonStr);
            newWatch.stop();
            long newSign = newWatch.getTotalTimeNanos();
            // 计算差值
            totalOld += oldSign;
            totalNew += (double) newSign / 1000000;
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
        result.put("id_list", idList);
        return result;
    }

    // 比较补签功能，n代表数据量大小，大于等于1的正整数
    @GetMapping("/supplementary/{n}")
    public  Map<String, Object> supplementary(@PathVariable int n){
        Random random = new Random();
        int totalOld = 0;
        double totalNew = 0;
        double totalDifference = 0;
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 初始化计时器
            StopWatch oldWatch = new StopWatch();
            StopWatch newWatch = new StopWatch();
            // 随机生成数据，并保存信息到 idList
            int userId=random.nextInt(350000);
            int month=random.nextInt(9) + 1;
            int day=random.nextInt(29) + 1;
            String id="t"+userId+":2024-0"+month;
            idList.add(id);
            String key="t"+userId+":2024:0"+month;
            StringBuilder signed = new StringBuilder();
            for (int i1 = 0; i1 < 32; i1++) {
                signed.append(random.nextInt(1));
            }
            // 新签到请求体
            Map<String, Object> json = new HashMap<>();
            json.put("id", id);
            json.put("key", key);
            json.put("day", day+"");
            json.put("signed", signed);
            String jsonStr=JSONUtil.parse(json).toString();
            // 老签到参数
            String str=id+"-"+day;
            // 先各跑一遍，去除连接数据库耗时影响
            try{
                // 删除已有数据
                updateById(new OldSign(str, 0));
                oldSignService.supplementary(str);
                // 重新删除已有数据
                updateById(new OldSign(str, 0));
                // 删除重复数据
                signed.setCharAt(day-1, '0');
                json.put("signed", signed.toString());
                jsonStr=JSONUtil.parse(json).toString();
                newSignService.supplementary(jsonStr);
                // 重新删除已有数据
                signed.setCharAt(day-1, '0');
                json.put("signed", signed.toString());
                jsonStr=JSONUtil.parse(json).toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            // 老签到计时
            oldWatch.start();
            oldSignService.supplementary(str);
            oldWatch.stop();
            long oldSign = oldWatch.getTotalTimeMillis();
            // 新签到计时
            newWatch.start();
            newSignService.supplementary(jsonStr);
            newWatch.stop();
            long newSign = newWatch.getTotalTimeNanos();
            // 计算差值
            totalOld += oldSign;
            totalNew += (double) newSign / 1000000;
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
        result.put("id_list", idList);
        return result;
    }
}
