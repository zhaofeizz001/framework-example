package com.zhaofei.framework.user.service.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author 
 * @email 
 * @date 2020-08-10 17:52:35
 */
@Data
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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
