package com.example.springboot.security.service;

import com.example.springboot.security.entity.UserInfo;

public interface UserService {

    UserInfo selectByPrimaryKey(Long id);
    int deleteByPrimaryKey(String id);
    int insert(UserInfo record);
    UserInfo selectByUserName(String userName);
}
