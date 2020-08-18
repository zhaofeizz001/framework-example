package com.zhaofei.framework.user.service.service.impl;

import com.zhaofei.framework.common.utils.JsonUtils;
import com.zhaofei.framework.common.utils.MD5Utils;
import com.zhaofei.framework.common.utils.RedisUtils;
import com.zhaofei.framework.user.api.constant.UserRedisKey;
import com.zhaofei.framework.user.api.entity.UserData;
import com.zhaofei.framework.user.api.entity.UserEntity;
import com.zhaofei.framework.user.api.exception.UserException;
import com.zhaofei.framework.user.api.service.UserService;
import com.zhaofei.framework.user.service.dao.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@DubboService
public class UserServiceImpl implements UserService {

    private static final String CONFUSION = "tx7yp6_%s_%s";

    @Autowired
    private UserDao userDao;

    @Override
    public String userLogin(UserEntity userEntity) throws UserException {
        UserEntity entity =userDao.selectByName(userEntity.getUsername());
        if(entity == null){
            throw new UserException();
        }
        String password = passwordEn(userEntity.getPassword(), entity.getSalt());
        if(entity.getPassword().equals(password)){
            String alreadyLogged = UserRedisKey.USER_ALREADY_LOGGED_KEY.getKey(entity.getUsername());
            String redisKey = RedisUtils.get(alreadyLogged);
            if(StringUtils.isEmpty(redisKey) || StringUtils.isEmpty(RedisUtils.get(UserRedisKey.USER_TOKEN_KEY.getKey(redisKey)))){
                String token = UUID.randomUUID().toString();
                String getKey = UserRedisKey.USER_TOKEN_KEY.getKey(token);
                UserData userData = UserData.newInstance(entity);
                userData.setLoginToken(token);
                RedisUtils.set(getKey, JsonUtils.objtoJson(userData), UserRedisKey.USER_TOKEN_KEY.getExpTime());
                RedisUtils.set(alreadyLogged, token, UserRedisKey.USER_ALREADY_LOGGED_KEY.getExpTime());

                return userData.getLoginToken();
            } else {
                return redisKey;
            }
        } else {
            throw new UserException();
        }
    }


    private String passwordEn(String reqPassword, String salt){
        if(StringUtils.isNotEmpty(reqPassword) && StringUtils.isNotEmpty(salt)){
            reqPassword = String.format(CONFUSION, reqPassword, salt);
            return MD5Utils.stringToMD5(MD5Utils.stringToMD5(reqPassword));
        } else {
            return "";
        }
    }
}
