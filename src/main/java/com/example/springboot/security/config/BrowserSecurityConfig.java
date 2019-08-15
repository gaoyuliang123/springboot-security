package com.example.springboot.security.config;

import com.example.springboot.security.handler.CustomLogOutSuccessHandler;
import com.example.springboot.security.session.CustomSessionExpiredStrategy;
import com.example.springboot.security.validate.code.ValidateCodeFilter;
import com.example.springboot.security.handler.CustomAuthenticationFailureHandler;
import com.example.springboot.security.handler.CustomAuthenticationSuccessHandler;
import com.example.springboot.security.service.impl.CustomUserDetailsServiceImpl;
import com.example.springboot.security.validate.smscode.SmsAuthenticationConfig;
import com.example.springboot.security.validate.smscode.SmsCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private SmsCodeFilter smsCodeFilter;
    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;

    @Autowired
    private CustomSessionExpiredStrategy customSessionExpiredStrategy;

    @Autowired
    private CustomLogOutSuccessHandler customLogOutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic() // Http Basic方式
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加自定义图片验证码过滤器
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加自定义短信验证码过滤器
                .formLogin() // 表单方式登录
//                .loginPage("/login.html") // 指定跳转到登录页面
                .loginPage("/authentication/require") // 指定跳转URL
                .loginProcessingUrl("/login") // 对应登录页面form表单的action="/login"
                .successHandler(customAuthenticationSuccessHandler) // 登录成功处理器
                .failureHandler(customAuthenticationFailureHandler) // 登录失败处理器
                .and()
                .logout() // 退出配置
                .logoutUrl("/signout")
//                .logoutSuccessUrl("/signout/success")
                .logoutSuccessHandler(customLogOutSuccessHandler)
                .deleteCookies("JSESSIONID")
                // 记住我功能配置
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository()) // 配置token持久化仓库
                .tokenValiditySeconds(3600) // token过期时间秒
                .userDetailsService(userDetailsService) // 处理自动登录逻辑
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/authentication/require",
                        "/login.html", "/css/login.css",
                        "/code/image",
                        "/code/sms",
                        "/session/invalid").permitAll() // 跳转到登录页面的请求不被拦截
//                .antMatchers("/**/*.{css,js}").permitAll() // 根据文件类型的请求不被拦截
                .anyRequest() // 所有请求
                .authenticated() // 都需要验证
                .and()
                .sessionManagement() // 添加Session管理器
                .invalidSessionUrl("/session/invalid") // Session失效跳转到指定的URL
                .maximumSessions(1) // Session并发控制
                //.maxSessionsPreventsLogin(true) // 当Session达到最大有效数的时候，不再允许相同的账户登录。
                .expiredSessionStrategy(customSessionExpiredStrategy); // Session在并发下失效后的处理策略

                http.csrf().disable() // 关闭CSRF攻击防御
                .apply(smsAuthenticationConfig); // 短信验证码认证配置加到Spring Security中
    }

    /**
     * Spring Security的记住我功能的实现需要使用数据库来持久化token
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 是否启动项目时创建保存token信息的数据表，这里设置为false，我们自己手动通过SQL创建。
        // 	JdbcTokenRepositoryImpl类中创建的SQL语句 CREATE_TABLE_SQL =
        // 	create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

}
