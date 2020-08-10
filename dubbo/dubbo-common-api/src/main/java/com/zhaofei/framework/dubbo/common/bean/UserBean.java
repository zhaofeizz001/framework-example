package com.zhaofei.framework.dubbo.common.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBean implements Serializable {

    public UserBean() {
    }

    public UserBean(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    private String name;

    private Integer age;
}
