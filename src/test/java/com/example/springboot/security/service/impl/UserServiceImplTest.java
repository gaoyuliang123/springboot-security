package com.example.springboot.security.service.impl;

import com.example.springboot.security.entity.UserInfo;
import com.example.springboot.security.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @decription: 文件描述
 * @author: admin
 * @date: 2019-08-14 21:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Test
    public void selectByPrimaryKey() {
    }

    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {
        UserInfo userInfo = new UserInfo("admin", "123456", "ADMIN");
        userService.insert(userInfo);
    }

    @Test
    public void selectByUserName() {
    }
}