package com.zhaofei.framework.cloud.provider.controller;

import com.zhaofei.framework.cloud.api.bean.UserBean;
import com.zhaofei.framework.cloud.api.service.UserClient;
import com.zhaofei.framework.cloud.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserClient {

    @Autowired
    private UserService userService;

    @Override
    public String sayHallo(UserBean userBean) {
        return userService.sayHallo(userBean);
    }
}
