package com.cascv.oas.server.user.model;

import java.util.HashSet;
import java.util.Set;

import com.cascv.oas.core.common.BaseEntity;

import lombok.Getter;
import lombok.Setter;

public class UserModel extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String uuid;          
	@Getter @Setter private String name;   
	@Getter @Setter private String nickname;
	@Getter @Setter private String password;
	@Getter @Setter private Integer inviteFrom; //邀请码
	@Getter @Setter private Integer inviteCode; //邀请码
	@Getter @Setter private String gender;     //性别
	@Getter @Setter private String birthday;   //生日
	@Getter @Setter private String address;    //地址
	@Getter @Setter private String mobile;     //手机号
	@Getter @Setter private String email;      //邮箱
	@Getter @Setter private String salt;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;
	@Getter @Setter private Integer mobileChecked;
	@Getter @Setter private Integer emailChecked;
	@Getter @Setter private Set<String> roles = new HashSet<>();
	@Getter @Setter private Set<String> perms = new HashSet<>();
}
