package com.cascv.oas.server.user.model;

import java.util.HashSet;
import java.util.Set;

import com.cascv.oas.core.common.BaseEntity;

import lombok.Getter;
import lombok.Setter;

public class UserModel extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private Integer id;       
	@Getter @Setter private String name;   
	@Getter @Setter private String nickname;
	@Getter @Setter private String password;
	@Getter @Setter private String salt;
	@Getter @Setter private String uuid;   
	@Getter @Setter private String created;   
	@Getter @Setter private String updated;
	@Getter @Setter private Set<String> roles = new HashSet<>();
	@Getter @Setter private Set<String> perms = new HashSet<>();
}
