package com.example.springboot.security.controller;

import com.example.springboot.security.entity.UserInfo;
import com.example.springboot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    public User getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (User)principal;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserInfo getAdminInfo() {
        return userService.selectByPrimaryKey(1L);
    }

    @PostMapping("/add/user")
    public Integer addUserInfo(UserInfo userInfo) {
        int result = userService.insert(userInfo);
        return result;
    }
}
