package com.zhaofei.framework.user.api.service;

import com.zhaofei.framework.user.api.entity.UserData;
import com.zhaofei.framework.user.api.exception.UserException;

public interface UserService {

    String userLogin(UserData userData) throws UserException;
}
