package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.vo.EnergyBallWrapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface EnergyBallMapper {
    EnergyBall selectByUuid(@Param("uuid") String uuid);

    List<EnergyBall> selectByTimeFuzzyQuery(@Param("userUuid") String userUuid,
                                            @Param("sourceCode") Integer sourceCode,
                                            @Param("created") String created);

    List<EnergyBall> selectByPointSourceCode(@Param("userUuid") String userUuid,
                                             @Param("sourceCode") Integer sourceCode,
                                             @Param("status") Integer status);

    EnergyBall selectLatestOneByPointSourceCode(@Param("userUuid") String userUuid,
                                                @Param("sourceCode") Integer sourceCode,
                                                @Param("status") Integer status);

    List<EnergyBallWrapper> selectPartByPointSourceCode(@Param("userUuid") String userUuid,
                                                        @Param("sourceCode") Integer sourceCode,
                                                        @Param("status") Integer status,
                                                        @Param("timeGap") Integer timeGap);

    int updateStatusByUuid(@Param("uuid") String uuid,
                           @Param("status") Integer status,
                           @Param("updated") String updated);

    int updatePointByUuid(@Param("uuid") String uuid,
                          @Param("point") BigDecimal point,
                          @Param("updated") String updated);

    int insertEnergyBall(EnergyBall energyBall);
    
    Integer countByUserUuidAndPowerSource(@Param("userUuid") String userUuid,@Param("sourceCode") Integer sourceCode);
    //查询一段时间内用户步行产生的积分总数
    Integer selectAllPointByTime(@Param("startTime") String startTime,@Param("endTime") String endTime);
}