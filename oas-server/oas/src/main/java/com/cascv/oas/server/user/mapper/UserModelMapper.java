package com.cascv.oas.server.user.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.wrapper.UserDetailModel;

@Component
public interface UserModelMapper {
	 List<UserDetailModel> selectUsersByPage( @Param("offset") Integer offset, @Param("limit") Integer limit,@Param("roleId")Integer roleId,@Param("searchValue")String searchValue); 
	 Integer countUsers( @Param("roleId") Integer roleId);
	 Integer updateUserStatus(@Param("status") Integer status,@Param("name") String name);
	 //UserIdentityCardModel inquireUserKYCInfo(@Param("userName") String userName);
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
	 UserModel selectByInviteFrom(@Param("inviteFrom") Integer InviteFrom);
	 UserModel selectSuperiorsUserByInviteFrom(@Param("inviteFrom") Integer InviteFrom);
	 Integer userInvitedCountTotal();

	 //验证码
	 Integer updateIdentifyCode(UserModel userModel);
     
	 //根据手机号查询用户名
	 String findUserByMobile(@Param("mobile") String mobile);
	 
	 //更改密码
	 Integer updateUserPassworde(UserModel userModel);
	 
	 Integer updateIMEI(UserModel userModel);
}

