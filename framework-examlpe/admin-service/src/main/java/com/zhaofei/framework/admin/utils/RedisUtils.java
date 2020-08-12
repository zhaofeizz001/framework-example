package com.zhaofei.framework.admin.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaofei.framework.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static RedisUtils redisUtil;

    @PostConstruct
    public void init() {
        redisUtil = this;
    }


    /**
     * 删除对应的value
     *
     * @param key
     */
    
    public static boolean remove(final String key) {
        if (exists(key)) {
            return redisUtil.stringRedisTemplate.delete(key);
        }
        return false;
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    
    public static boolean exists(final String key) {
        return redisUtil.stringRedisTemplate.hasKey(key);
    }

    
    public static boolean set(String key, String value) {
        redisUtil.stringRedisTemplate.opsForValue().set(key, value);
        return true;
    }


    
    public static boolean set(final String key, final String value, final Long expireTime) {
        redisUtil.stringRedisTemplate.opsForValue().set(key, value,expireTime,TimeUnit.SECONDS);
        return true;
    }

    
    public static String get(final String key) {
        return redisUtil.stringRedisTemplate.opsForValue().get(key);
    }

    
    public static boolean expire(final String key, long expire) {
        return redisUtil.stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    
    public static <T> boolean setList(String key, List<T> list) {
        String value = JsonUtils.objtoJson(list);
        return set(key, value);
    }

    
    public static <T> boolean setList(final String key, final List<T> list, final Long expireTime) {
        String value = JsonUtils.objtoJson(list);
        return set(key, value, expireTime);
    }

    
    public static <T> List<T> getList(final String key, final Class<T> clz) {
        String json = get(key);
        if(json != null){
            List<T> list = null;
            try {
                list = JSONObject.parseArray(json, clz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    
    public static long lpush(final String key, final Object obj) {
        final String value = JsonUtils.objtoJson(obj);
        long result = redisUtil.stringRedisTemplate.execute(new RedisCallback<Long>() {
            
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisUtil.stringRedisTemplate.getStringSerializer();
                long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    
    public static long rpush(final String key, final Object obj) {
        final String value = JsonUtils.objtoJson(obj);
        long result = redisUtil.stringRedisTemplate.execute(new RedisCallback<Long>() {
            
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisUtil.stringRedisTemplate.getStringSerializer();
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    
    public static String lpop(final String key) {
        String result = redisUtil.stringRedisTemplate.execute(new RedisCallback<String>() {
            
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisUtil.stringRedisTemplate.getStringSerializer();
                byte[] res =  connection.lPop(serializer.serialize(key));
                return serializer.deserialize(res);
            }
        });
        return result;
    }
    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return
     */
    
    public static Long TTL(String key) {
        return redisUtil.stringRedisTemplate.getExpire(key);
    }

    
    public static boolean saveObject(String key, Object obj) {
        String json = JsonUtils.objtoJson(obj);
        return set(key, json);
    }

    
    public static boolean saveObject(String key, Object obj, Long expireTime) {
        String json = JsonUtils.objtoJson(obj);
        return set(key, json, expireTime);
    }

    
    public static <T> T getObject(String key, Class<T> clz) {
        String json = get(key);
        if(json != null){
            try {
                return JSONObject.parseObject(json, clz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    
    public static Set<String> keys(String pattern) {
        return redisUtil.stringRedisTemplate.keys(pattern);
    }

    
    public static void addMap(String key, String field, String value){
        redisUtil.stringRedisTemplate.opsForHash().put(getStr(key), getStr(field), value);
    }

    
    public static <T> void addMap(String key, String field, T obj){
        redisUtil.stringRedisTemplate.opsForHash().put(getStr(key), getStr(field), obj);
    }

    
    public static <T> T getMap(String key, Class<T> clazz){
        BoundHashOperations<String, String, String> boundHashOperations = redisUtil.stringRedisTemplate.boundHashOps(getStr(key));
        Map<String, String> map = boundHashOperations.entries();
        return JSON.parseObject(JSON.toJSONString(map), clazz);
    }

    
    public static <T> T getMapField(String key, String field, Class<T> clazz){
        return (T)redisUtil.stringRedisTemplate.boundHashOps(getStr(key)).get(getStr(field));
    }

    
    public static void delMapField(String key, String... field){
        BoundHashOperations<String, String, ?> boundHashOperations = redisUtil.stringRedisTemplate.boundHashOps(getStr(key));
        String[] strArr = new String[field.length];
        for (int i = 0; i < field.length; i++) {
            strArr[i] = getStr(field[i]);
        }
        boundHashOperations.delete(strArr);
    }

    private static String getStr(String s){
        return isNumeric(s) ? "s" + s : s;
    }

    public static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }
}