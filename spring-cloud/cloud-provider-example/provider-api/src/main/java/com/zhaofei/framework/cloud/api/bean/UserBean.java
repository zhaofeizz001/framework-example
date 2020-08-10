package com.zhaofei.framework.cloud.api.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBean implements Serializable {

    private String name;

    private Integer age;
}
