package com.zhaofei.framework.common.aop;

import com.zhaofei.framework.common.base.entity.BaseUserBean;
import com.zhaofei.framework.common.constant.CommonRedisKey;
import com.zhaofei.framework.common.utils.JsonUtils;
import com.zhaofei.framework.common.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.zhaofei.framework.common.utils.JsonUtils.jsonToList;
import static com.zhaofei.framework.common.utils.JsonUtils.jsonToMap;

@Aspect
@Component
public class CacheAspect {


    @Pointcut("execution(public * com.zhaofei.framework.*.service.service.impl..*.select*(..))")
    public void controllerMethod() {
    }


    @Around("controllerMethod()")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Exception {
        Signature sig = pjp.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException();
        }
        msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());

        String[] s = msig.getMethod().toGenericString().split(" ");
        String methodAddr = s[s.length - 1];

        BaseUserBean userBean = null;
        for (int i = 0; i < pjp.getArgs().length; i++){
            Object arg = pjp.getArgs()[i];
            if(arg instanceof BaseUserBean){
                userBean = (BaseUserBean) arg;
                break;
            }
        }

        String redisKeyCache = CommonRedisKey.getCommonMethodCacheByDesc(methodAddr)
                .getKey(userBean.getUsername(), methodAddr).replaceAll(" ", "");
        String redisKey = redisKeyCache.substring(0, redisKeyCache.indexOf("("));
        int x = redisKey.lastIndexOf(".");
        char[] redisKeyChar = redisKey.toCharArray();
        redisKeyChar[x] = ':';
        redisKey = String.valueOf(redisKeyChar);
        redisKey += redisKeyCache.substring(redisKeyCache.indexOf("("));

        String result = RedisUtils.get(redisKey);
        if(StringUtils.isNotEmpty(result)){
            return returnRedisVal(result, currentMethod);
        }

        try {
            Object o =  pjp.proceed();
            String redisVal;
            if(o instanceof Collection || o instanceof Map || o instanceof BaseUserBean){
                redisVal = JsonUtils.objtoJson(o);
            }else {
                redisVal = o.toString();
            }
            RedisUtils.set(redisKey, redisVal, CommonRedisKey.getCommonMethodCacheByDesc(methodAddr).getExpTime());
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Object returnRedisVal(String redisVal, Method currentMethod){
        Type genericReturnType = currentMethod.getGenericReturnType();
        Type[] type = ((ParameterizedType) genericReturnType).getActualTypeArguments();
        if(currentMethod.getReturnType().getSimpleName().equals("List")){
            if(genericReturnType instanceof ParameterizedType && type.length == 1){
                return jsonToList(redisVal, (Class) type[0]);
            } else {
                return jsonToList(redisVal, Object.class);
            }
        } else if(currentMethod.getReturnType().getSimpleName().equals("Set")){
            if(genericReturnType instanceof ParameterizedType && type.length == 1){
                return new HashSet<>(jsonToList(redisVal, (Class) type[0]));
            } else {
                return new HashSet<>(jsonToList(redisVal, Object.class));
            }
        } else if(currentMethod.getReturnType().getSimpleName().equals("Map")){
            if(genericReturnType instanceof ParameterizedType){
                return jsonToMap(redisVal, (Class) type[0], (Class) type[1]);
            } else {
                return jsonToMap(redisVal);
            }

        } else {
            return getObject(redisVal, (Class) genericReturnType);
        }
    }


    @SuppressWarnings("unchecked")
    private static  <T> Object getObject(Object o, Class<T> tClass){
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
        return (T) o;
    }
}
