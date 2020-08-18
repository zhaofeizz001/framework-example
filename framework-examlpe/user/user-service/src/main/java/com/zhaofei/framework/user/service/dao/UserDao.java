package com.zhaofei.framework.user.service.dao;


import com.zhaofei.framework.user.api.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author 
 * @email 
 * @date 2020-08-10 17:52:35
 */
@Mapper
public interface UserDao {
    UserEntity selectByName(@Param("username")String username);
}
