package com.example.springboot.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @decription: 文件描述
 * @author: admin
 * @date: 2019-08-14 22:39
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = -136637405839144078L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
