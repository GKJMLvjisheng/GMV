package com.cascv.oas.server.walk.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.walk.model.WalkBall;
import com.cascv.oas.server.walk.wrapper.WalkBallReturn;

@Component
public interface WalkMapper {
	
	WalkBall selectWalkBall(@Param("userUuid") String userUuid, 
			@Param("status") Integer status);
	Integer updateStatusByUuid(@Param("uuid") String uuid,
            @Param("status") Integer status,
            @Param("updated") String updated);
	
	WalkBallReturn selectEnergyBallList(@Param("userUuid") String userUuid, 
			@Param("sourceCode") Integer sourceCode, @Param("status") Integer status);
	
	Integer insertWalkBall(WalkBall walkBall);

}
