package com.example.springboot.security.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }

    @GetMapping("/index")
    public Object index() {
        // 获取登录用户信息
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
