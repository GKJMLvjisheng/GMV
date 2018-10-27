package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class UserDetailModel implements Serializable {
	private static final long serialVersionUID = 1L;         
	@Getter @Setter private String name;   
	@Getter @Setter private String nickname;
	@Getter @Setter private String gender;     //性别
	@Getter @Setter private String birthday;   //生日
	@Getter @Setter private String address;    //地址
	@Getter @Setter private String mobile;     //手机号
	@Getter @Setter private String email;      //邮箱
	@Getter @Setter private String created;
	@Getter @Setter private String IMEI;
	@Getter @Setter private Integer status;
	@Getter @Setter private Integer roleId;
	@Getter @Setter private String userIdentityName;
	@Getter @Setter private String userIdentityNumber;
	@Getter @Setter private String remark;
	@Getter @Setter private Integer verifyStatus;
}
