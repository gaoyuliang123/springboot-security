package com.example.springboot.security.validate.smscode;

import com.example.springboot.security.common.Constant;
import com.example.springboot.security.exception.ValidateCodeException;
import com.example.springboot.security.validate.code.ImageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义短信验证码过滤器
 */
@Component
public class SmsCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 拦截登录请求进行短信验证码校验
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/login/mobile".equalsIgnoreCase(request.getRequestURI()) &&
                HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            try {
                validateCode(new ServletWebRequest(request));
            } catch (ServletRequestBindingException e) {
                e.printStackTrace();
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 校验验证码
     */
    private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException, ValidateCodeException {
        String mobile = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "mobile");
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "smsCode");
        SmsCode codeInSession = (SmsCode)sessionStrategy.getAttribute(servletWebRequest, Constant.SESSION_KEY_SMS_CODE + mobile);

        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCodeException("短信验证码不能为空!");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("短信验证码不存在，请重新发送!");
        }
        if (codeInSession.isExpire()) {
            throw new ValidateCodeException("短信验证码已过期，请重新发送!");
        }
        if (!codeInRequest.equals(codeInSession.getCode())) {
            throw new ValidateCodeException("短信验证码不正确!");
        }
        sessionStrategy.removeAttribute(servletWebRequest, Constant.SESSION_KEY_IMAGE_CODE);
    }
}
