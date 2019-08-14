package com.example.springboot.security.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 模拟登录账户
 */
@Getter
@Setter
public class CustomUser implements Serializable {

    private static final long serialVersionUID = 3497935880426858541L;

    private String userName;

    private String password;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked= true;

    private boolean credentialsNonExpired= true;

    private boolean enabled= true;
}
