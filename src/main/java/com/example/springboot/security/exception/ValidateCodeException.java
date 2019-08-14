package com.example.springboot.security.exception;

import javax.security.sasl.AuthenticationException;

/**
 * @decription: 文件描述
 * @author: admin
 * @date: 2019-08-14 22:39
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 5022575393500654459L;

    ValidateCodeException(String message) {
        super(message);
    }
}
