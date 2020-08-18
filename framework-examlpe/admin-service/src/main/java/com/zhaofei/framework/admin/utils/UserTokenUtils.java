package com.zhaofei.framework.admin.utils;

import com.zhaofei.framework.common.utils.JsonUtils;
import com.zhaofei.framework.common.utils.RedisUtils;
import com.zhaofei.framework.user.api.constant.UserRedisKey;
import com.zhaofei.framework.common.base.entity.UserData;
import com.zhaofei.framework.user.api.exception.UserException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.zhaofei.framework.admin.constant.ResponseCode.USER_INFO_EXCEPTION;

public class UserTokenUtils {

    private static String TOKEN = "token";

    public static UserData getUserInfo() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(TOKEN);
        String userJson = RedisUtils.get(UserRedisKey.USER_TOKEN_KEY.getKey(token));
        try {
            UserData userData = JsonUtils.jsonToBean(userJson, UserData.class);
            if(userData == null){
                throw new Exception();
            }
            return userData;
        } catch (Exception e) {
            throw new UserException(USER_INFO_EXCEPTION);
        }
    }
}
