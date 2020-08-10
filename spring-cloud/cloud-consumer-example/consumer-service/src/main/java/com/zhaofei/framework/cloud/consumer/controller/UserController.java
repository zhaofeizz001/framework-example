package com.zhaofei.framework.cloud.consumer.controller;

import com.zhaofei.framework.cloud.api.bean.UserBean;
import com.zhaofei.framework.cloud.api.service.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserClient userService;

    @RequestMapping(value = "/sayHallo/{name}/{age}")
    public String sayHallo(@PathVariable String name, @PathVariable Integer age) {
        UserBean userBean = new UserBean();
        userBean.setName(name);
        userBean.setAge(age);
        return userService.sayHallo(userBean);
    }
}
