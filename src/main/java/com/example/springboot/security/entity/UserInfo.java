package com.example.springboot.security.entity;

import lombok.Data;

@Data
public class UserInfo {

    private Long id;
    private String userName;
    private String password;
    private String role;

    public Long getId() {
        return id;
    }

    public UserInfo() {
    }

    public UserInfo(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public UserInfo(Long id, String userName, String password, String role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

}
