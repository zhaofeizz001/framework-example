package com.zhaofei.framework.dubbo.controller;

import com.zhaofei.framework.dubbo.common.bean.UserBean;
import com.zhaofei.framework.dubbo.common.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class UserController {

    @DubboReference
    private UserService userService;

    @GetMapping("/sayHallo/{name}/{age}")
    public String sayHallo(@PathVariable String name, @PathVariable Integer age){
        UserBean userBean = new UserBean(name, age);
        return userService.sayHello(userBean);
    }
}
