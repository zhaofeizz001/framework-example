package com.zhaofei.framework.admin.bean.res;

import lombok.Data;

@Data
public class UserResponseLoginBean {

    public UserResponseLoginBean(String token, Long expTime) {
        this.token = token;
        this.expTime = expTime == null ? "-1" : expTime.toString();
    }

    // token
    private String token;
    // 过期时间
    private String expTime;
}
