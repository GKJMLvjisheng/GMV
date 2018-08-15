package com.cascv.oas.server.user.mapper;


import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.user.model.UserModel;

public interface UserModelMapper {
	 Integer insertUser(UserModel userModel);
	 UserModel selectByName(@Param("name") String name);
	 Integer deleteUser(String id);
}
