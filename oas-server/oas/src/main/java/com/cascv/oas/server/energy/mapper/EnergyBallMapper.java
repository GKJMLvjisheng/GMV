package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.dto.EnergyBallWithTime;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EnergyBallMapper {
    /**
     * 根据 id 与时间模糊查询
     * @param energyBallWithTime
     * @return
     */
    List<EnergyBall> selectByTimeFuzzyQuery(EnergyBallWithTime energyBallWithTime);

    /**
     * 查询出所有未被获取过能量的能量球
     * @return
     */
    List<EnergyBall> selectByStatus();

    /**
     * 插入新鲜的能量球
     * @param energyBall
     * @return
     */
    int insertEnergyBall(EnergyBall energyBall);

    /**
     * 修改能量球的状态，由1-0，表示该球已被获取过能量
     * @param id
     * @return
     */
    int updateEnergyBallStatusById(String id);

    /**
     * 根据能量球id，查询能量球信息
     * @param id
     * @return
     */
    EnergyBall selectById(String id);
}