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
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: binghujuelie
 * Date: 2023/10/9
 * Time: 13:53
 */
@RestController
@Slf4j
@RequestMapping("/costCompare")
@CrossOrigin
@SuppressWarnings("unchecked")
public class CostCompareController extends ServiceImpl<OldSignMapper, OldSign> {
    @Resource
    private IOldSignService oldSignService;
    @Resource
    private INewSignService newSignService;

    // 比较获取签到列表，n代表数据量大小，大于等于1的正整数
    @GetMapping("/getSignList/{n}")
    public Map<String, Object> getSignList(@PathVariable int n) {
        // 初始化计时器
        StopWatch oldWatch = new StopWatch();
        StopWatch newWatch = new StopWatch();
        // 存储新老签到总的耗时
        double totalOld = 0;
        double totalNew = 0;
        // 存储所有的主键 id
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 随机生成查询参数字符串，并保存到 idList
            Map<String, Object> params = getRandomParams(false);
            Map<String, Object> json = (Map<String, Object>) params.get("newSignParam");
            String id = json.get("id").toString();
            idList.add(id);
            // 先各跑一遍，去除连接数据库耗时影响
            oldSignService.getSignedList(id);
            newSignService.getNewSignDTO(id);
            // 老签到计时
            oldWatch.start();
            oldSignService.getSignedList(id);
            oldWatch.stop();
            long oldSign = oldWatch.getLastTaskTimeNanos();
            // 新签到计时
            newWatch.start();
            newSignService.getNewSignDTO(id);
            newWatch.stop();
            long newSign = newWatch.getLastTaskTimeNanos();
            // 分别累加总时间
            totalOld += oldSign;
            totalNew += newSign;
        }
        return getResultMap(totalOld, totalNew, idList);
    }

    // 比较获取签到次数，n代表数据量大小，大于等于1的正整数
    @GetMapping("/getSignedCount/{n}")
    public Map<String, Object> getSignedCount(@PathVariable int n) {
        // 初始化计时器
        StopWatch oldWatch = new StopWatch();
        StopWatch newWatch = new StopWatch();
        // 存储新老签到总的耗时
        double totalOld = 0;
        double totalNew = 0;
        // 存储所有的主键 id
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 随机生成查询参数字符串，并保存到 idList
            Map<String, Object> params = getRandomParams(false);
            Map<String, Object> json = (Map<String, Object>) params.get("newSignParam");
            String id = json.get("id").toString();
            idList.add(id);
            // 先各跑一遍，去除连接数据库耗时影响
            oldSignService.signedCount(id);
            newSignService.getSignedCount(id);
            // 老签到计时
            oldWatch.start();
            oldSignService.signedCount(id);
            oldWatch.stop();
            long oldSign = oldWatch.getLastTaskTimeNanos();
            // 新签到计时
            newWatch.start();
            newSignService.getSignedCount(id);
            newWatch.stop();
            long newSign = newWatch.getLastTaskTimeNanos();
            // 分别累加总时间
            totalOld += oldSign;
            totalNew += newSign;
        }
        return getResultMap(totalOld, totalNew, idList);
    }

    // 比较获取最大连续签到次数，n代表数据量大小，大于等于1的正整数
    @GetMapping("/maxContinue/{n}")
    public Map<String, Object> maxContinue(@PathVariable int n) {
        // 初始化计时器
        StopWatch oldWatch = new StopWatch();
        StopWatch newWatch = new StopWatch();
        // 存储新老签到总的耗时
        double totalOld = 0;
        double totalNew = 0;
        // 存储所有的主键 id
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 随机生成查询参数字符串，并保存到 idList
            Map<String, Object> params = getRandomParams(false);
            Map<String, Object> json = (Map<String, Object>) params.get("newSignParam");
            String id = json.get("id").toString();
            idList.add(id);
            // 先各跑一遍，去除连接数据库耗时影响
            oldSignService.maxContinue(id);
            newSignService.maxContinue(id);
            // 老签到计时
            oldWatch.start();
            oldSignService.maxContinue(id);
            oldWatch.stop();
            long oldSign = oldWatch.getLastTaskTimeNanos();
            // 新签到计时
            newWatch.start();
            newSignService.maxContinue(id);
            newWatch.stop();
            long newSign = newWatch.getLastTaskTimeNanos();
            // 分别累加总时间
            totalOld += oldSign;
            totalNew += newSign;
        }
        return getResultMap(totalOld, totalNew, idList);
    }

    // 比较签到功能，n代表数据量大小，大于等于1的正整数
    @GetMapping("/sign/{n}")
    public Map<String, Object> sign(@PathVariable int n) {
        // 初始化计时器
        StopWatch oldWatch = new StopWatch();
        StopWatch newWatch = new StopWatch();
        // 存储新老签到总的耗时
        double totalOld = 0;
        double totalNew = 0;
        // 存储所有的主键 id
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 随机生成查询参数字符串，并保存到 idList
            Map<String, Object> params = getRandomParams(true);
            String oldSignParam = params.get("oldSignParam").toString();
            Map<String, Object> json = (Map<String, Object>) params.get("newSignParam");
            String newSignParam = JSONUtil.parse(json).toString();
            idList.add(oldSignParam);
            // 删除已有数据
            deleteTestData(oldSignParam, json);
            // 先各跑一遍，去除连接数据库耗时影响
            oldSignService.sign(oldSignParam);
            newSignService.sign(newSignParam);
            // 重新删除已有数据
            deleteTestData(oldSignParam, json);
            // 老签到计时
            oldWatch.start();
            oldSignService.sign(oldSignParam);
            oldWatch.stop();
            long oldSign = oldWatch.getLastTaskTimeNanos();
            // 新签到计时
            newWatch.start();
            newSignService.sign(newSignParam);
            newWatch.stop();
            long newSign = newWatch.getLastTaskTimeNanos();
            // 删除测试数据
            deleteTestData(oldSignParam, json);
            // 分别累加总时间
            totalOld += oldSign;
            totalNew += newSign;
        }
        return getResultMap(totalOld, totalNew, idList);
    }

    // 比较补签功能，n代表数据量大小，大于等于1的正整数
    @GetMapping("/supplementary/{n}")
    public Map<String, Object> supplementary(@PathVariable int n) {
        // 初始化计时器
        StopWatch oldWatch = new StopWatch();
        StopWatch newWatch = new StopWatch();
        // 存储新老签到总的耗时
        double totalOld = 0;
        double totalNew = 0;
        // 存储所有的主键 id
        ArrayList<String> idList = new ArrayList<>();
        // 循环次数由请求链接中参数n决定
        for (int i = 0; i < n; i++) {
            // 随机生成查询参数字符串，并保存到 idList
            Map<String, Object> params = getRandomParams(true);
            String oldSignParam = params.get("oldSignParam").toString();
            Map<String, Object> json = (Map<String, Object>) params.get("newSignParam");
            String newSignParam = JSONUtil.parse(json).toString();
            idList.add(oldSignParam);
            // 删除已有数据
            deleteTestData(oldSignParam, json);
            // 先各跑一遍，去除连接数据库耗时影响
            oldSignService.supplementary(oldSignParam);
            newSignService.supplementary(newSignParam);
            // 重新删除已有数据
            deleteTestData(oldSignParam, json);
            // 老签到计时
            oldWatch.start();
            oldSignService.supplementary(oldSignParam);
            oldWatch.stop();
            long oldSign = oldWatch.getLastTaskTimeNanos();
            // 新签到计时
            newWatch.start();
            newSignService.supplementary(newSignParam);
            newWatch.stop();
            long newSign = newWatch.getLastTaskTimeNanos();
            // 删除测试数据
            deleteTestData(oldSignParam, json);
            // 分别累加总时间
            totalOld += oldSign;
            totalNew += newSign;
        }
        return getResultMap(totalOld, totalNew, idList);
    }

    // 获取随机生成的参数，参数输入false为给查询操作生成参数
    private Map<String, Object> getRandomParams(boolean isUpdate) {
        // 初始化随机数工具
        Random random = new Random();
        // 随机生成数据，并保存信息到 idList
        int userId = random.nextInt(350000);
        int month = random.nextInt(9) + 1;
        int day = random.nextInt(29) + 1;
        String id = isUpdate ? "t" + userId + ":2024-0" + month : "t" + userId + ":2023-0" + month;
        String key = isUpdate ? "t" + userId + ":2024:0" + month : "t" + userId + ":2023:0" + month;
        StringBuilder signed = new StringBuilder();
        for (int i1 = 0; i1 < 32; i1++) {
            signed.append(random.nextInt(1));
        }
        // 新签到请求体
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("key", key);
        json.put("day", day + "");
        json.put("signed", signed);
        // 老签到参数
        String str = id + "-" + day;
        // 返回随机生成的参数 Map
        Map<String, Object> params = new HashMap<>();
        params.put("oldSignParam", str);
        params.put("newSignParam", json);
        return params;
    }

    // 从数据库删除测试添加的数据，防止测试数据运行失败
    private void deleteTestData(String oldSignParam, Map<String, Object> newSignJson) {
        // 老签到
        removeById(new OldSign(oldSignParam, 0));
        // 新签到
        StringBuilder builder = new StringBuilder(newSignJson.get("signed").toString());
        builder.setCharAt(Integer.parseInt(newSignJson.get("day").toString()), '0');
        newSignJson.put("signed", builder.toString());
        String strJson = JSONUtil.parse(newSignJson).toString();
        newSignService.sign(strJson);
    }

    // 获取返回Map
    private Map<String, Object> getResultMap(double totalOld, double totalNew, ArrayList<String> idList) {
        Map<String, Object> result = new HashMap<>();
        double totalDifference = totalOld - totalNew;
        // 老签到总耗时
        result.put("old_sign_total", totalOld / 1000000);
        // 新签到总耗时
        result.put("new_sign_total", totalNew / 1000000);
        // 新老签到总耗时差
        result.put("difference_total", totalDifference / 1000000);
        // 性能提升比率
        double rate = totalDifference / totalOld;
        result.put("optimized_rate", new DecimalFormat("0.00").format(rate * 100) + "%");
        // 请求参数列表
        result.put("id_list", idList);
        return result;
    }
}
