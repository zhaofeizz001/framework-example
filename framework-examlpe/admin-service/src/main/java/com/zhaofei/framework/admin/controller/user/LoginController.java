package com.zhaofei.framework.admin.controller.user;

import com.zhaofei.framework.admin.bean.req.UserRequestLoginBean;
import com.zhaofei.framework.admin.bean.res.UserResponseLoginBean;
import com.zhaofei.framework.admin.utils.UserTokenUtils;
import com.zhaofei.framework.common.base.controller.AbstractController;
import com.zhaofei.framework.common.base.entity.RestResult;
import com.zhaofei.framework.common.utils.RedisUtils;
import com.zhaofei.framework.user.api.constant.UserRedisKey;
import com.zhaofei.framework.user.api.entity.UserData;
import com.zhaofei.framework.user.api.entity.UserEntity;
import com.zhaofei.framework.user.api.exception.UserException;
import com.zhaofei.framework.user.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/user")
@RestController
public class LoginController extends AbstractController {

    @DubboReference(check = false)
    private UserService userService;

    @PostMapping("/login")
    public RestResult<UserResponseLoginBean> userLogin(@RequestBody @Validated UserRequestLoginBean userRequestLoginBean) throws Exception{
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userRequestLoginBean, userEntity);
        try {
            String token = userService.userLogin(userEntity);
            return this.genSuccessResult(new UserResponseLoginBean(token, UserRedisKey.USER_TOKEN_KEY.getExpTime()));
        } catch (UserException e) {
            return this.genErrorResult(e.getCode());
        }
    }


    @PostMapping("/getUserInfo")
    public RestResult<UserData> getUserInfo(){
        try {
            return this.genSuccessResult(UserTokenUtils.getUserInfo());
        } catch (UserException e) {
            return this.genErrorResult(e.getCode());
        }
    }

    @PostMapping("/logout")
    public RestResult userLogout(HttpServletRequest request) throws Exception {
        UserData userData = UserTokenUtils.getUserInfo();
        if(StringUtils.isNotEmpty(userData.getLoginToken())){
            RedisUtils.remove(UserRedisKey.USER_TOKEN_KEY.getKey(userData.getLoginToken()));
        }
        if(userData != null){
            RedisUtils.remove(UserRedisKey.USER_ALREADY_LOGGED_KEY.getKey(userData.getUsername()));
        }
        return this.genSuccessResult();
    }
}
