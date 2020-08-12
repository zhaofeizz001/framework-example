package com.zhaofei.framework.admin.filter;

import com.zhaofei.framework.admin.utils.RedisUtils;
import com.zhaofei.framework.common.utils.CookieUtils;
import com.zhaofei.framework.user.api.constant.UserRedisKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    private final static  String LOGIN_URL = "/login";

    private final static String INDEX_URL = "/index";

    private final static String TOKEN = "token";

    private final static String PUBLIC_URL = "/statics";

    private static List<String> EXCLUDE_URL = new ArrayList<>();

    static {
        EXCLUDE_URL.add("/login");
        EXCLUDE_URL.add("/user/login");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        String cookieValue = CookieUtils.getCookieValue(httpServletRequest, TOKEN);
        if(EXCLUDE_URL.contains(httpServletRequest.getRequestURI())){
            if(StringUtils.isNotEmpty(cookieValue)){
                ((HttpServletResponse) response).sendRedirect(INDEX_URL);
                return;
            }
            chain.doFilter(request, response);
        } else if(httpServletRequest.getRequestURI().startsWith(PUBLIC_URL)){
            chain.doFilter(request, response);
        }else {
            if(StringUtils.isEmpty(cookieValue)){
                ((HttpServletResponse) response).sendRedirect(LOGIN_URL);
                return;
            } else {
                boolean exists = RedisUtils.exists(UserRedisKey.USER_TOKEN_KEY.getKey(cookieValue));
                if(exists){
                    chain.doFilter(request, response);
                } else {
                    CookieUtils.deleteCookie((HttpServletRequest) request, (HttpServletResponse)response, TOKEN);
                    ((HttpServletResponse) response).sendRedirect(LOGIN_URL);
                    return;
                }

            }
        }

    }

    @Override
    public void destroy() {

    }
}
