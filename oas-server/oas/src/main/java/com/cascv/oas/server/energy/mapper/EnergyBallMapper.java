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
                                            @Param("pointSource") Integer pointSource,
                                            @Param("timeCreated") String timeCreated);

    List<EnergyBall> selectByPointSourceCode(@Param("userUuid") String userUuid,
                                             @Param("pointSource") Integer pointSource,
                                             @Param("status") Integer status);

    EnergyBall selectLatestOneByPointSourceCode(@Param("userUuid") String userUuid,
                                                @Param("pointSource") Integer pointSource,
                                                @Param("status") Integer status);

    List<EnergyBallWrapper> selectPartByPointSourceCode(@Param("userUuid") String userUuid,
                                                        @Param("pointSource") Integer pointSource,
                                                        @Param("status") Integer status,
                                                        @Param("timeGap") Integer timeGap);

    int updateStatusByUuid(@Param("uuid") String uuid,
                           @Param("status") Integer status,
                           @Param("timeUpdated") String timeUpdated);

    int updatePointByUuid(@Param("uuid") String uuid,
                          @Param("point") BigDecimal point,
                          @Param("timeUpdated") String timeUpdated);

    int insertEnergyBall(EnergyBall energyBall);
    
    Integer countByUserUuidAndPowerSource(@Param("userUuid") String userUuid,@Param("powerSource") Integer powerSource);
}