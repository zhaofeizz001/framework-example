package com.zhaofei.framework.admin.controller.user;

import com.zhaofei.framework.admin.bean.req.UserRequestLoginBean;
import com.zhaofei.framework.admin.bean.res.UserResponseLoginBean;
import com.zhaofei.framework.common.base.controller.AbstractController;
import com.zhaofei.framework.common.base.entity.RestResult;
import com.zhaofei.framework.user.api.constant.UserRedisKey;
import com.zhaofei.framework.user.api.entity.UserData;
import com.zhaofei.framework.user.api.exception.UserException;
import com.zhaofei.framework.user.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class LoginController extends AbstractController {

    @DubboReference
    private UserService userService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public RestResult<UserResponseLoginBean> userLogin(@RequestBody @Validated UserRequestLoginBean userRequestLoginBean) throws Exception{
        UserData userData = new UserData();
        BeanUtils.copyProperties(userRequestLoginBean, userData);
        try {
            String token = userService.userLogin(userData);
            return this.genSuccessResult(new UserResponseLoginBean(token, UserRedisKey.USER_TOKEN_KEY.getExpTime()));
        } catch (UserException e) {
            return this.genErrorResult(e.getCode());
        }
    }
}
