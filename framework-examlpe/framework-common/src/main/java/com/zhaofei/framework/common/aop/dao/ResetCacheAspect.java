package com.zhaofei.framework.common.aop;

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

@Aspect
@Component
public class ResetCacheAspect {

    private static final String SERVICE_IMPL = "ServiceImpl";

    private static final String _KEY = "%s:selectOneById%s";

    private static final String REDIS_KEY = "%s-%s";


    @Pointcut("execution(public * com.zhaofei.framework.*.service.service.impl..*.insert*(..))")
    public void insertMethod() {
    }

    @Pointcut("execution(public * com.zhaofei.framework.*.service.service.impl..*.update*(..))")
    public void updateMethod() {
    }

    @Pointcut("execution(public * com.zhaofei.framework.*.service.service.impl..*.delete*(..))")
    public void deleteMethod() {
    }


    @Around("insertMethod()")
    public Object insertMethod(ProceedingJoinPoint pjp) throws Throwable {
        return removeRedisCache(pjp);
    }

    @Around("updateMethod()")
    public Object updateMethod(ProceedingJoinPoint pjp) throws Throwable {
        return removeRedisCache(pjp);
    }

    @Around("deleteMethod()")
    public Object deleteMethod(ProceedingJoinPoint pjp) throws Throwable {
        return removeRedisCache(pjp);
    }

    private Object removeRedisCache(ProceedingJoinPoint pjp) throws Throwable {
        String simpleName = pjp.getTarget().getClass().getSimpleName().replace(SERVICE_IMPL,"");
        String redisKey = AopCacheUtils.getRedisCacheKey(pjp);
        int index = redisKey.lastIndexOf(":");
        String redisCacheKey = redisKey.substring(0, index);
        String sss = redisKey.substring(redisKey.indexOf("("));
        redisCacheKey = String.format(_KEY, redisCacheKey, sss);
        Object proceed = pjp.proceed();
        String methodAddr = AopCacheUtils.getMethodAddr(pjp);
        System.out.println("调用数据库数据，更新缓存");
        String json = JsonUtils.objtoJson(proceed);
        Map<String, Object> stringObjectMap = JsonUtils.jsonToGenericMap(json, new TypeReference<Map<String, Object>>() {
        });
        Object id = stringObjectMap.get("id");
        String redisSaveKey = String.format(REDIS_KEY, redisCacheKey, id);
        RedisUtils.set(redisSaveKey, json, CommonRedisKey.getCommonMethodCacheByDesc(redisCacheKey).getExpTime());

        return proceed;
    }
}
