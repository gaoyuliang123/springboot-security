package com.example.springboot.security.service.impl;

import com.example.springboot.security.dao.UserInfoMapper;
import com.example.springboot.security.entity.UserInfo;
import com.example.springboot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserInfo selectByPrimaryKey(Long id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int deleteByPrimaryKey(String id) {
        return userInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int insert(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo selectByUserName(String userName) {
        return userInfoMapper.selectByUserName(userName);
    }
}
