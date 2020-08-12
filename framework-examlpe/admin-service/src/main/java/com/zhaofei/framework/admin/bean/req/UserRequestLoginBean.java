package com.zhaofei.framework.admin.bean.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequestLoginBean implements Serializable {
    // 账号
    private String username;
    // 密码
    private String password;
    // 验证码
    private String code;

}
