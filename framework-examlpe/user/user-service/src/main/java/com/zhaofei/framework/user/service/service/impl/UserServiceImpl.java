package com.zhaofei.framework.user.service.service.impl;

import com.zhaofei.framework.common.utils.JsonUtils;
import com.zhaofei.framework.common.utils.MD5Utils;
import com.zhaofei.framework.user.api.constant.UserRedisKey;
import com.zhaofei.framework.user.api.entity.UserData;
import com.zhaofei.framework.user.api.exception.UserException;
import com.zhaofei.framework.user.api.service.UserService;
import com.zhaofei.framework.user.service.dao.UserDao;
import com.zhaofei.framework.user.service.entity.UserEntity;
import com.zhaofei.framework.user.service.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@DubboService
public class UserServiceImpl implements UserService {

    private static final String CONFUSION = "tx7yp6_%s";

    @Autowired
    private UserDao userDao;

    @Override
    public String userLogin(UserData userData) throws UserException {
        UserEntity entity =userDao.selectByName(userData.getUsername());
        if(entity == null){
            throw new UserException();
        }
        String password = passwordEn(userData.getPassword(), entity.getSalt());
        String token = "";
        if(entity.getPassword().equals(password)){
            String alreadyLogged = UserRedisKey.USER_ALREADY_LOGGED_KEY.getKey(entity.getUsername());
            String redisKey = RedisUtils.get(alreadyLogged);
            if(StringUtils.isEmpty(redisKey)){
                token = UUID.randomUUID().toString();
                String getKey = UserRedisKey.USER_TOKEN_KEY.getKey(token);
                RedisUtils.set(getKey, JsonUtils.objtoJson(entity), UserRedisKey.USER_TOKEN_KEY.getExpTime());
                RedisUtils.set(alreadyLogged, token, UserRedisKey.USER_ALREADY_LOGGED_KEY.getExpTime());
            } else {
                return redisKey;
            }
        }
        return token;
    }


    private String passwordEn(String reqPassword, String salt){
        if(StringUtils.isNotEmpty(reqPassword) && StringUtils.isNotEmpty(salt)){
            reqPassword = String.format(CONFUSION, reqPassword);
            reqPassword = reqPassword + salt;
            return MD5Utils.stringToMD5(MD5Utils.stringToMD5(reqPassword));
        } else {
            return "";
        }
    }
}
