package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.vo.EnergyBallWrapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EnergyBallMapper {
    List<EnergyBall> selectByTimeFuzzyQuery(@Param("userUuid")String userUuid,
                                            @Param("pointSourceCode")Integer pointSourceCode,
                                            @Param("timeCreated")String timeCreated);

    List<EnergyBall> selectByPointSourceCode(@Param("userUuid")String userUuid,
                                             @Param("pointSourceCode")Integer pointSourceCode);

    List<EnergyBallWrapper> selectPartByPointSourceCode(@Param("userUuid")String userUuid,
                                                     @Param("pointSourceCode")Integer pointSourceCode);

    int updateStatusByUuid(@Param("uuid")String uuid,
                           @Param("status")Integer status,
                           @Param("timeUpdated")String timeUpdated);

    int insertEnergyBall(EnergyBall energyBall);
}