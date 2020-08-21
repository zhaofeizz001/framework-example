/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.zhaofei.framework.admin.config;

import com.zhaofei.framework.admin.filter.MethodLoginInterceptor;
import com.zhaofei.framework.admin.filter.ViewLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * WebMvc配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/view/index").setViewName("index");
        registry.addViewController("/view/welcome").setViewName("welcome");
        registry.addViewController("/view/login").setViewName("login");
        registry.addViewController("/view/article_list").setViewName("article_list");
        registry.addViewController("/view/article_add").setViewName("article_add");
    }

    private static final String LOGIN_VIEW = "/view/login";
    private static final String LOGIN_METHOD = "/user/login";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration viewLogin = registry.addInterceptor(new ViewLoginInterceptor());
        viewLogin.addPathPatterns("/");
        viewLogin.addPathPatterns("/view/**");
        viewLogin.excludePathPatterns(LOGIN_VIEW);

        InterceptorRegistration methodLogin = registry.addInterceptor(new MethodLoginInterceptor());
        methodLogin.addPathPatterns("/user/**");
        methodLogin.addPathPatterns("/article/**");
        methodLogin.excludePathPatterns(LOGIN_METHOD);
    }
}