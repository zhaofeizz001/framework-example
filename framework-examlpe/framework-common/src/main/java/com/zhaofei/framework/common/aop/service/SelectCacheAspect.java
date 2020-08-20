package com.zhaofei.framework.common.aop.service;

import com.zhaofei.framework.common.aop.util.AopCacheUtils;
import com.zhaofei.framework.common.base.entity.BaseBean;
import com.zhaofei.framework.common.base.entity.PageResponseBean;
import com.zhaofei.framework.common.constant.CommonRedisKey;
import com.zhaofei.framework.common.utils.JsonUtils;
import com.zhaofei.framework.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import static com.zhaofei.framework.common.utils.JsonUtils.jsonToList;
import static com.zhaofei.framework.common.utils.JsonUtils.jsonToMap;


/**
 * 查询缓存 当然也可以使用dubbo filter来实现，更为方便
 */
@Slf4j
@Aspect
@Component
public class SelectCacheAspect {


    @Pointcut("execution(public * com.zhaofei.framework.*.service.service.impl..*.select*(..))")
    public void controllerMethod() {
    }


    @Around("controllerMethod()")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Exception {
        MethodSignature methodSignature = AopCacheUtils.getMethodSignature(pjp);
        Method currentMethod = AopCacheUtils.getCurrentMethod(pjp, methodSignature);
        String redisCacheKey = AopCacheUtils.getRedisCacheKey(pjp);

        String result = RedisUtils.get(redisCacheKey);
        if(StringUtils.isNotEmpty(result)){
            System.out.println("读取缓存信息");
            return returnRedisVal(result, currentMethod);
        }

        try {
            System.out.println("调用数据库数据");
            Object o =  pjp.proceed();
            String redisVal;
            if(o instanceof Collection
                    || o instanceof Map
                    || o instanceof BaseBean
                    || o instanceof PageResponseBean){
                redisVal = JsonUtils.objtoJson(o);
            }else {
                redisVal = o.toString();
            }
            String methodAddr = AopCacheUtils.getMethodAddr(pjp);
            RedisUtils.set(redisCacheKey, redisVal, CommonRedisKey.getCommonMethodCacheByDesc(methodAddr).getExpTime());
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Object returnRedisVal(String redisVal, Method currentMethod) throws Exception{
        Type genericReturnType = currentMethod.getGenericReturnType();
        if(currentMethod.getReturnType().getSimpleName().equals("List")){
            Type[] type = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            if(genericReturnType instanceof ParameterizedType && type.length == 1){
                return jsonToList(redisVal, (Class) type[0]);
            } else {
                return jsonToList(redisVal, Object.class);
            }
        } else if(currentMethod.getReturnType().getSimpleName().equals("Set")){
            Type[] type = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            if(genericReturnType instanceof ParameterizedType && type.length == 1){
                return new HashSet<>(jsonToList(redisVal, (Class) type[0]));
            } else {
                return new HashSet<>(jsonToList(redisVal, Object.class));
            }
        } else if(currentMethod.getReturnType().getSimpleName().equals("Map")){
            if(genericReturnType instanceof ParameterizedType){
                Type[] type = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                return jsonToMap(redisVal, (Class) type[0], (Class) type[1]);
            } else {
                return jsonToMap(redisVal);
            }

        }else {
            try {
                return getObject(redisVal, (Class) genericReturnType);
            } catch (Exception e) {
                log.error("", e);
                Class<?> rawType = ((ParameterizedTypeImpl) genericReturnType).getRawType();
                return getObject(redisVal, rawType);
            }
        }
    }


    @SuppressWarnings("unchecked")
    private static  <T> Object getObject(Object o, Class<T> tClass) throws Exception{
        if(tClass.isAssignableFrom(String.class)){
            return o.toString();
        }
        if(tClass.isAssignableFrom(Integer.class)){
            return Integer.parseInt(o.toString());
        }
        if(tClass.isAssignableFrom(Long.class)){
            return Long.parseLong(o.toString());
        }
        if(tClass.isAssignableFrom(Double.class)){
            return Double.parseDouble(o.toString());
        }
        if(tClass.isAssignableFrom(Float.class)){
            return Float.parseFloat(o.toString());
        }
        if(tClass.isAssignableFrom(Short.class)){
            return Short.parseShort(o.toString());
        }
        if(tClass.isAssignableFrom(char.class) || tClass.isAssignableFrom(Character.class)){
            return Character.valueOf(o.toString().charAt(0));
        }
        if(tClass.isAssignableFrom(Byte.class)){
            return Byte.parseByte(o.toString());
        }
        if(tClass.isAssignableFrom(Boolean.class)){
            return Boolean.parseBoolean(o.toString());
        }
        return JsonUtils.jsonToBean(o.toString(), tClass);
    }

}
