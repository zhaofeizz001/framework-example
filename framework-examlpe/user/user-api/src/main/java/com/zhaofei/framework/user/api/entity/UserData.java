package com.zhaofei.framework.common.base.entity;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserData extends BaseBean implements Serializable {

	public UserData(){}

	public static UserData newInstance(Object o){
		UserData userData = new UserData();
		BeanUtils.copyProperties(o, userData);
		return userData;
	}

	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 混淆盐
	 */
	private String salt;
	/**
	 * 头像地址
	 */
	private String avatarAddress;
	/**
	 * 登录时间
	 */
	private Date loginTime;
	/**
	 * 上一次登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 是否启用：0.禁用  1.开启
	 */
	private Integer state;
	/**
	 * 用户登录token
	 */
	private String loginToken;
}
