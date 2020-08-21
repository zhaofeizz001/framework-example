package com.zhaofei.framework.common.aop.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhaofei.framework.common.base.entity.PageRequestBean;
import com.zhaofei.framework.common.base.entity.PageResponseBean;
import com.zhaofei.framework.common.constant.CommonRedisKey;
import com.zhaofei.framework.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;

public class AopCacheUtils {

    private static final String REDIS_KEY = "%s-%s";
    private static final String REDIS_PAGE_KEY = "%s-%s-%s";

    public static MethodSignature getMethodSignature(ProceedingJoinPoint pjp){
        Signature sig = pjp.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException();
        }
        return (MethodSignature) sig;
    }

    public static Method getCurrentMethod(ProceedingJoinPoint pjp, MethodSignature methodSignature) throws Exception{
        Object target = pjp.getTarget();
        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        return currentMethod;
    }

    public static String getMethodAddr(ProceedingJoinPoint pjp){
        MethodSignature methodSignature = getMethodSignature(pjp);
        String[] s = methodSignature.getMethod().toGenericString().split(" ");
        return s[s.length - 1];
    }

    public static String getRedisCacheKey(ProceedingJoinPoint pjp) throws Exception{

        String methodAddr = getMethodAddr(pjp);
        Object arg = pjp.getArgs()[0];

        String json = JsonUtils.objtoJson(arg);
        Map<String, Object> stringObjectMap = JsonUtils.jsonToGenericMap(json, new TypeReference<Map<String, Object>>() {
        });
        String username = String.valueOf(stringObjectMap.get("username"));
        String id = String.valueOf(stringObjectMap.get("id"));

        String redisKeyCache = CommonRedisKey.getCommonMethodCacheByDesc(methodAddr)
                .getKey(username, methodAddr).replaceAll(" ", "");
        String redisKey = redisKeyCache.substring(0, redisKeyCache.indexOf("("));
        int x = redisKey.lastIndexOf(".");
        char[] redisKeyChar = redisKey.toCharArray();
        redisKeyChar[x] = ':';
        redisKey = String.valueOf(redisKeyChar);
        redisKey += redisKeyCache.substring(redisKeyCache.indexOf("("));
        if(StringUtils.isNotEmpty(id) && !"null".equals(id)){
            return String.format(REDIS_KEY, redisKey, id);
        } else if(arg instanceof PageRequestBean){
            PageRequestBean pageRequestBean = (PageRequestBean) arg;
            return String.format(REDIS_PAGE_KEY, redisKey, pageRequestBean.getPageSize(), pageRequestBean.getPageNum());

        }
        return redisKey;
    }
}
