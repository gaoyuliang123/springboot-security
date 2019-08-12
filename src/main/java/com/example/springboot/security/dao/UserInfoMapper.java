package com.example.springboot.security.dao;

import com.example.springboot.security.entity.UserInfo;

public interface UserInfoMapper {

    UserInfo selectByPrimaryKey(Long id);
    int deleteByPrimaryKey(String id);
    int insert(UserInfo record);
    UserInfo selectByUserName(String userName);

}
