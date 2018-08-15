package com.cascv.oas.server.user.mapper;


import com.cascv.oas.server.user.model.UserModel;

public interface UserModelMapper {
	 Integer insertUser(UserModel userModel);
	 UserModel selectByName(String name);
	 Integer deleteUser(String id);
}
