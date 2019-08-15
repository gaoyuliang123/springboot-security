package com.example.springboot.security.validate.smscode;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义短信验证拦截器
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Getter
    @Setter
    private String mobileParameter = "mobile";

    protected SmsAuthenticationFilter() {
        // 当请求为/login/mobile，请求方法为POST的时候该过滤器生效。
        super(new AntPathRequestMatcher("/login/mobile", HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String mobile = obtainMobile(request);
        if (mobile == null) {
            mobile = "";
        }
        SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken(mobile);
        setDetails(request, smsAuthenticationToken);
        return this.getAuthenticationManager().authenticate(smsAuthenticationToken);
    }

    private void setDetails(HttpServletRequest request, SmsAuthenticationToken smsAuthenticationToken) {
        smsAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }
}
