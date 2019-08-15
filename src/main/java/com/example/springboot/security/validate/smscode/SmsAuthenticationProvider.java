package com.example.springboot.security.validate.smscode;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 自定义支持处理SmsAuthenticationToken该类型Token的Provider类
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {

    @Getter
    @Setter
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 具体的身份认证逻辑
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken)authentication;
        String moblie = (String) smsAuthenticationToken.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(moblie);
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("未找到与该手机号" + moblie + "对应的用户");
        }
        SmsAuthenticationToken authenticationTokenResult = new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationTokenResult.setDetails(smsAuthenticationToken.getDetails());
        return authenticationTokenResult;
    }

    /**
     * 指定支持处理的Token类型为SmsAuthenticationToken
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
