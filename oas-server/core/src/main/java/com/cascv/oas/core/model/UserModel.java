package com.cascv.oas.core.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cascv.oas.core.common.BaseEntity;

import lombok.Getter;
import lombok.Setter;

public class UserModel extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter @Setter private Long id;       // 用户id
	@Getter @Setter private String name;   	// 登录名，不可改
	@Getter @Setter private String nickname;    // 用户昵称，可改
	@Getter @Setter private String password;// 已加密的登录密码
	@Getter @Setter private String salt;    // 加密盐值
	@Getter @Setter private Date created;   // 创建时间
	@Getter @Setter private Date updated;   // 修改时间
	@Getter @Setter private Set<String> roles = new HashSet<>();    //用户所有角色值，用于shiro做角色权限的判断
	@Getter @Setter private Set<String> perms = new HashSet<>();    //用户所有权限值，用于shiro做资源权限的判断
}
