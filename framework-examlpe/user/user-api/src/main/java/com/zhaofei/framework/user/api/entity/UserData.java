package com.zhaofei.framework.user.api.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserData implements Serializable {

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

}
