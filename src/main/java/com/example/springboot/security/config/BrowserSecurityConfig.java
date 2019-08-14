package com.example.springboot.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        // 基于内存
        // auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("123456")).roles();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic() // Http Basic方式
        http.formLogin() // 表单方式登录
//                .loginPage("/login.html") // 指定跳转到登录页面
                .loginPage("/authentication/require") // 指定跳转URL
                .loginProcessingUrl("/login") // 对应登录页面form表单的action="/login"
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/authentication/require", "/login.html", "/css/login.css").permitAll() // 跳转到登录页面的请求不被拦截
//                .antMatchers("/**/*.{css,js}").permitAll()
                .anyRequest() // 所有请求
                .authenticated() // 都需要验证
                .and()
                .csrf().disable(); // 关闭CSRF攻击防御
    }
}
