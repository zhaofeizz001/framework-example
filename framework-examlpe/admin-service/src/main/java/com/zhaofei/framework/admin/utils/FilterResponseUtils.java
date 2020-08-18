package com.zhaofei.framework.admin.utils;

import com.zhaofei.framework.common.base.entity.Header;
import com.zhaofei.framework.common.base.entity.RestResult;
import com.zhaofei.framework.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

@Component
public class FilterResponseUtils {

    @Autowired
    ResourceBundleMessageSource errorMessageSource;

    private static ResourceBundleMessageSource messageSource;

    private static FilterResponseUtils filterResponseUtils;

    @PostConstruct
    public void init(){
        filterResponseUtils = this;
        messageSource = errorMessageSource;
    }

    public static boolean notLoginResponse(HttpServletResponse response, String responseCode) throws Exception{
        RestResult restResult = RestResult.newInstance();
        Header header = new Header();
        header.setRetCode(responseCode);
        String message = messageSource.getMessage(responseCode, null, response.getLocale());
        header.setRetMsg(message);
        restResult.setHeader(header);
        String result = JsonUtils.objtoJson(restResult);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(result);
        return false;
    }
}
