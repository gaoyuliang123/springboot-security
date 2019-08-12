package com.example.springboot.security.config;

import com.example.springboot.security.enums.Role;
import com.example.springboot.security.filter.BeforLoginFilter;
import com.example.springboot.security.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity //开启Spring Security的功能
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启Spring方法级安全 决定Spring Security的前注解、保障注解是否可用;JSR-250  注解是否可用。
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置为从内存中进行加载认证信息,在内存中创建了用户admin/user，密码为123456。
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                // 指定加密方式
//                .password(passwordEncoder().encode("123456"))
//                .roles(Role.ADMIN.name());
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password(passwordEncoder().encode("123456"))
//                .roles(Role.NORMAL.name());
        // 基于数据库的身份认证和角色授权
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                // 允许post请求/add/user访问（用户注册）
//                .antMatchers(HttpMethod.POST, "/add/user").permitAll()
//                // 所有请求都需要验证
//                .anyRequest().authenticated()
//                .and()
//                // 使用默认的登录页面
//                .formLogin()
//                .and()
//                // post请求要关闭csrf验证,不然访问报错
//                .csrf().disable();
        http  // 添加自定义过滤器
                .addFilterBefore(new BeforLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                // 允许所有人都可以访问登录页面
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
