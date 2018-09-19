package com.cascv.oas.server.user.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.user.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public interface UserModelMapper {
	 Integer insertUser(UserModel userModel);
	 UserModel selectByName(@Param("name") String name);
	 UserModel selectByUuid(@Param("uuid") String uuid);
	 Integer deleteUser(String uuid);
	 UserModel selectByInviteCode(@Param("inviteCode") Integer inviteCode);
	 
	/*
	 * Name:upadateUserInfo
	 * Author:lvjisheng
	 * Date:2018.09.03
	 */	 
	 Integer updateUserInfo(UserModel userModel);
	 
	/*
	 * Name:upadateUserProfile
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */	
	 Integer updateUserProfile(UserModel userModel);
	 
	/*
	 * Name:resetMobileByName
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */	
	 Integer resetMobileByName(UserModel userModel);
	/*
	 * Name:resetMobileByName
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */	
	 Integer resetEmailByName(UserModel userModel);
	 
	 List<String> selectUserMail(String email);
	 
	 List<String> selectUserMobile(String mobile);
	 
	/*
	 * Name:selectByInviteFrom		
	 * Author:yangming
	 * Date:2018.09.19
	 */	
	 UserModel selectByInviteFrom(@Param("InviteFrom") Integer InviteFrom);

	 //验证码
	 Integer updateIdentifyCode(UserModel userModel);

}

