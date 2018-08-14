package com.cascv.oas.server.mapper;


import com.cascv.oas.core.model.UserModel;

public interface UserModelMapper {
	 Integer insertUser(UserModel userModel);
	 Integer deleteUser(String id);
}
