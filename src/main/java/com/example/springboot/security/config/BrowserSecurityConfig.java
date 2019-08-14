package com.example.springboot.security.config;

import com.example.springboot.security.handler.CustomAuthenticationFailureHandler;
import com.example.springboot.security.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic() // Http Basic方式
        http.formLogin() // 表单方式登录
//                .loginPage("/login.html") // 指定跳转到登录页面
                .loginPage("/authentication/require") // 指定跳转URL
                .loginProcessingUrl("/login") // 对应登录页面form表单的action="/login"
                .successHandler(customAuthenticationSuccessHandler) // 登录成功处理器
                .failureHandler(customAuthenticationFailureHandler) // 登录失败处理器
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/authentication/require", "/login.html", "/css/login.css", "/code/image").permitAll() // 跳转到登录页面的请求不被拦截
//                .antMatchers("/**/*.{css,js}").permitAll()
                .anyRequest() // 所有请求
                .authenticated() // 都需要验证
                .and()
                .csrf().disable(); // 关闭CSRF攻击防御
    }
}
