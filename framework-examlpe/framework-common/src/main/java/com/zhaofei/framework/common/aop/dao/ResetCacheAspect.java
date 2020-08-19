package com.zhaofei.framework.common.aop.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhaofei.framework.common.aop.util.AopCacheUtils;
import com.zhaofei.framework.common.constant.CommonRedisKey;
import com.zhaofei.framework.common.utils.JsonUtils;
import com.zhaofei.framework.common.utils.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;


/**
 * 更新缓存 当然也可以使用dubbo filter来实现，更为方便
 */
@Aspect
@Component
public class ResetCacheAspect {

    private static final String SELECT_ONE_KEY = "%s:selectOneById%s";
    private static final String SELECT_LIST_KEY = "%s:selectList%s";

    private static final String REDIS_KEY = "%s-%s";


//    @Pointcut("execution(public * com.zhaofei.framework.*.service.service.impl..*.insert*(..))")
//    public void insertMethod() {
//    }
//
    @Pointcut("execution(public * com.zhaofei.framework.*.service.service.impl..*.insert*(..))" +
            " || execution(public * com.zhaofei.framework.*.service.service.impl..*.update*(..))")
    public void exMethod() {
    }

    @Pointcut("execution(public * com.zhaofei.framework.*.service.service.impl..*.delete*(..))")
    public void deleteMethod() {
    }


    @Around("exMethod()")
    public Object exMethod(ProceedingJoinPoint pjp) throws Throwable {
        return removeRedisCache(pjp, false);
    }

    @Around("deleteMethod()")
    public Object deleteMethod(ProceedingJoinPoint pjp) throws Throwable {
        return removeRedisCache(pjp, true);
    }

    private Object removeRedisCache(ProceedingJoinPoint pjp, boolean isDelete) throws Throwable {
        String redisKey = AopCacheUtils.getRedisCacheKey(pjp);
        int index = redisKey.lastIndexOf(":");
        String redisCacheKey = redisKey.substring(0, index);
        String sss = redisKey.substring(redisKey.indexOf("("));
        redisCacheKey = String.format(SELECT_ONE_KEY, redisCacheKey, sss);
        Object proceed = pjp.proceed();
        System.out.println("调用数据库数据，更新缓存");
        String json = JsonUtils.objtoJson(proceed);
        Map<String, Object> stringObjectMap = JsonUtils.jsonToGenericMap(json, new TypeReference<Map<String, Object>>() {
        });
        Object id = stringObjectMap.get("id");
        String redisSaveKey = String.format(REDIS_KEY, redisCacheKey, id);
        if(isDelete){
            RedisUtils.remove(redisSaveKey);
        } else {
            RedisUtils.set(redisSaveKey, json, CommonRedisKey.getCommonMethodCacheByDesc(redisCacheKey).getExpTime());
        }

        cleanPageListCache(redisKey.substring(0, index));

        return proceed;
    }

    private void cleanPageListCache(String redisKey){
        Set<String> keys = RedisUtils.keys(String.format(SELECT_LIST_KEY, redisKey, "*"));
        keys.forEach(key -> {
            RedisUtils.remove(key);
        });
    }
}
