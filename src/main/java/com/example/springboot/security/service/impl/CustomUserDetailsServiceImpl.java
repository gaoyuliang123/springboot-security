package com.example.springboot.security.service.impl;

import com.example.springboot.security.entity.UserInfo;
import com.example.springboot.security.model.CustomUser;
import com.example.springboot.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 模拟一个用户，代数据库获取逻辑
//        CustomUser customUser = new CustomUser();
//        customUser.setUserName(userName);
//        customUser.setPassword(this.passwordEncoder.encode("123456"));
//        // 7个参数的构造器
//        User customDetail = new User(customUser.getUserName(), customUser.getPassword(), customUser.isEnabled(),
//                customUser.isAccountNonExpired(), customUser.isCredentialsNonExpired(), customUser.isAccountNonLocked(),
//                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        // 从数据库获取用户信息
        UserInfo userInfo = userService.selectByUserName(userName);
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new UsernameNotFoundException("用户名=" + userName + "不存在");
        }
        // 获取权限列表
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 角色必须以`ROLE_`开头!
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userInfo.getRole()));
        User userDetail = new User(userInfo.getUserName(), userInfo.getPassword(), authorities);
        return userDetail;
    }
}
