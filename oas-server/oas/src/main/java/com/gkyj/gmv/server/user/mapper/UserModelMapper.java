package com.gkyj.gmv.server.user.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.gkyj.gmv.server.user.model.UserModel;

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

