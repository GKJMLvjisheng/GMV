package com.cascv.oas.server.user.mapper;


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
}

