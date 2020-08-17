package com.zhaofei.framework.common.aop;

import com.zhaofei.framework.common.aop.util.AopCacheUtils;
import com.zhaofei.framework.common.utils.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
public class DeleteCacheAspect {


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
        Object proceed = pjp.proceed();
        removeRedisCache(pjp);
        return proceed;
    }

    @Around("updateMethod()")
    public Object updateMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        removeRedisCache(pjp);
        return proceed;
    }

    @Around("deleteMethod()")
    public Object deleteMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        removeRedisCache(pjp);
        return proceed;
    }

    private void removeRedisCache(ProceedingJoinPoint pjp) throws Throwable {
        String redisCacheKey = AopCacheUtils.getRedisCacheKey(pjp);
        redisCacheKey = redisCacheKey.substring(0, redisCacheKey.lastIndexOf(":"));
        redisCacheKey = redisCacheKey + ":select*";
        Set<String> keys = RedisUtils.keys(redisCacheKey);
        keys.forEach(key -> {
            boolean exists = RedisUtils.exists(key);
            if (exists) {
                RedisUtils.remove(key);
            }
        });
    }
}
