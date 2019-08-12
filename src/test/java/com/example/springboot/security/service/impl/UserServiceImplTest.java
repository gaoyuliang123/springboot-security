package com.example.springboot.security.service.impl;

import com.example.springboot.security.entity.UserInfo;
import com.example.springboot.security.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void selectByPrimaryKey() {
        userService.selectByPrimaryKey(1L);
    }

    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {
        UserInfo userInfo = new UserInfo("user", passwordEncoder.encode("123456"), "NORMAL");
        userService.insert(userInfo);
    }

    @Test
    public void selectByUserName() {
    }
}