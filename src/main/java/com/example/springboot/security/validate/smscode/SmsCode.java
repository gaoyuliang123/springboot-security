package com.example.springboot.security.validate.smscode;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短信验证码
 */
@Getter
@Setter
public class SmsCode implements Serializable {

    private String code;
    private LocalDateTime expireTime;

    public SmsCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public SmsCode(String code, long expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
