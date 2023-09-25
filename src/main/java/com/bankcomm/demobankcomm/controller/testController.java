package com.bankcomm.demobankcomm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/22
 * Time: 10:20
 */

@Slf4j
@RestController
public class testController {

    // 测试用
    @GetMapping("/test")
    public String test() {
        return "hello world";
    }
}

