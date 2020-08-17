package com.zhaofei.framework.admin.filter;

import com.zhaofei.framework.admin.utils.RedisUtils;
import com.zhaofei.framework.common.utils.CookieUtils;
import com.zhaofei.framework.user.api.constant.UserRedisKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewInterceptor implements HandlerInterceptor {
    long start = System.currentTimeMillis();

    private final static  String LOGIN_URL = "/view/login";

    private final static String TOKEN = "token";



    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String cookieValue = CookieUtils.getCookieValue(httpServletRequest, TOKEN);

        if(StringUtils.isEmpty(cookieValue)){
            httpServletResponse.sendRedirect(LOGIN_URL);
        } else {
            boolean exists = RedisUtils.exists(UserRedisKey.USER_TOKEN_KEY.getKey(cookieValue));
            if(!exists){
                CookieUtils.deleteCookie(httpServletRequest, httpServletResponse, TOKEN);
                httpServletResponse.sendRedirect(LOGIN_URL);
            }

        }

        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}