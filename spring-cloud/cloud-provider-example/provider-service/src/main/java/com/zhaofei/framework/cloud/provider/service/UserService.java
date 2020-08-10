package com.zhaofei.framework.cloud.provider.service;

import com.zhaofei.framework.cloud.api.bean.UserBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(name = "provider-service")
public interface UserService {

    @RequestMapping(value = "/sayHallo")
    String sayHallo(@RequestBody UserBean userBean);
}
