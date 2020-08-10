package com.zhaofei.framework.cloud.provider.service.impl;

import com.zhaofei.framework.cloud.api.bean.UserBean;
import com.zhaofei.framework.cloud.provider.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final static String STRING_TEMPLATE = "Hello %s, your age is %s";

    @Override
    public String sayHallo(UserBean userBean) {
        return String.format(STRING_TEMPLATE, userBean.getName(), userBean.getAge());
    }
}
