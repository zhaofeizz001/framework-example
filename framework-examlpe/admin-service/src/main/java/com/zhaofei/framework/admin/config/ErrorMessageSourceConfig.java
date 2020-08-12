package com.zhaofei.framework.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 自定义错误提示信息
 *
 */
@Configuration
public class ErrorMessageSourceConfig {

    @Bean(name = "errorMessageSource")
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        ResourceBundleMessageSource errorMessageSource = new ResourceBundleMessageSource();
        errorMessageSource.addBasenames("messages/messages");
        return errorMessageSource;
    }
}
