package com.zhaofei.framework.user.api.service;

import com.zhaofei.framework.user.api.entity.UserEntity;
import com.zhaofei.framework.user.api.exception.UserException;

public interface UserService {

    String userLogin(UserEntity userEntity) throws UserException;
}
