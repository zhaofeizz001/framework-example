package com.zhaofei.framework.dubbo.provider.service.impl;

import com.zhaofei.framework.dubbo.common.bean.UserBean;
import com.zhaofei.framework.dubbo.common.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class UserServiceImpl implements UserService {

    private final static String STRING_TEMPLATE = "Hello %s, your age is %s";
    @Override
    public String sayHello(UserBean userBean) {
        return String.format(STRING_TEMPLATE, userBean.getName(), userBean.getAge());
    }
}
