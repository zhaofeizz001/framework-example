package com.zhaofei.framework.admin.filter;

import com.zhaofei.framework.admin.utils.FilterResponseUtils;
import com.zhaofei.framework.common.utils.RedisUtils;
import com.zhaofei.framework.user.api.constant.UserRedisKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zhaofei.framework.admin.constant.ResponseCode.USER_NOT_LOGIN;

public class MethodLoginInterceptor implements HandlerInterceptor {

    private final static String TOKEN = "token";


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String cookieValue = httpServletRequest.getHeader(TOKEN);
        if(StringUtils.isEmpty(cookieValue)){
            return FilterResponseUtils.notLoginResponse(httpServletResponse, USER_NOT_LOGIN);
        } else {
            boolean exists = RedisUtils.exists(UserRedisKey.USER_TOKEN_KEY.getKey(cookieValue));
            if(!exists){
               return FilterResponseUtils.notLoginResponse(httpServletResponse, USER_NOT_LOGIN);
            }
        }
        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}