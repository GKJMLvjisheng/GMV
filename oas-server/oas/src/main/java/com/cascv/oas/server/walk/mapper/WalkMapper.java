package com.cascv.oas.server.walk.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.walk.model.WalkBall;
import com.cascv.oas.server.walk.wrapper.WalkBallReturn;

@Component
public interface WalkMapper {
	
	List<WalkBall> selectWalkBall(@Param("userUuid") String userUuid, 
			@Param("status") Integer status);
	Integer updateStatusByUuid(@Param("uuid") String uuid,
            @Param("status") Integer status,
            @Param("updated") String updated);
	Integer updateStepNumByCreated(@Param("userUuid") String userUuid,
			@Param("stepNum") BigDecimal stepNum,
            @Param("created") String created);
	Integer updatePointByuuid(@Param("uuid") String uuid,
            @Param("point") BigDecimal point);
	
	WalkBall selectWalkBallbyUuid(@Param("uuid") String uuid);
	WalkBall selectTodayWalkBall(@Param("userUuid") String userUuid);
	
	List<WalkBallReturn> selectEnergyBallList(@Param("userUuid") String userUuid, 
			@Param("sourceCode") Integer sourceCode, @Param("status") Integer status);
	
	Integer insertWalkBall(WalkBall walkBall);

}
