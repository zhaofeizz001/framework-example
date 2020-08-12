package com.zhaofei.framework.common.base.controller;

import com.zhaofei.framework.common.base.entity.Header;
import com.zhaofei.framework.common.base.entity.RestResult;
import com.zhaofei.framework.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public abstract class AbstractController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ResourceBundleMessageSource errorMessageSource;

    /**
     * normal
     *
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public <T> RestResult<T> genResult(String retCode, T data, String message) {
        RestResult<T> result = RestResult.newInstance();
        Locale locale = LocaleContextHolder.getLocale();
        if (message == null) {
            message = errorMessageSource.getMessage(retCode, null, locale);
        }
        Header header = new Header(retCode, message);

        result.setHeader(header);
        result.setBody(data);
        result.setTime(System.currentTimeMillis() + "");
        logger.info("获取返回信息：{}", JsonUtils.objtoJson(result));
        return result;

    }

    public <T> RestResult<T> genResult(String retCode, T data) {
        return genResult(retCode, data, null);
    }

    /**
     * success
     *
     * @param data
     * @param <T>
     * @return
     */
    public <T> RestResult<T> genSuccessResult( T data) {
        return genResult("0", data);
    }

    /**
     * success
     *
     * @param <T>
     * @return
     */
    public <T> RestResult<T> genSuccessResult() {
        return genResult("0", null);
    }

    /**
     * error message
     *
     * @param <T>
     * @return
     */
    public <T> RestResult<T> genErrorResult(String errorCode) {
        return genResult(errorCode, null, null);
    }

    /**
     * error message
     *
     * @param <T>
     * @return
     */
    public <T> RestResult<T> genErrorResult( String errorCode, T data) {
        return genResult(errorCode ,data, null);
    }

    /**
     * error message
     *
     * @param <T>
     * @return
     */
    public <T> RestResult<T> genErrorResult( String errorCode, String message) {
        return genResult(errorCode ,null, message);
    }
}
