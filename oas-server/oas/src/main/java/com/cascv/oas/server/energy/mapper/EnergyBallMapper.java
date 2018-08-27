package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergyBallExample;
import com.cascv.oas.server.energy.vo.EnergyBallWithTime;
import org.apache.ibatis.annotations.Param;
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

    long countByExample(EnergyBallExample example);
    int deleteByExample(EnergyBallExample example);
    int deleteByPrimaryKey(Integer id);
    int insert(EnergyBall record);
    int insertSelective(EnergyBall record);
    List<EnergyBall> selectByExample(EnergyBallExample example);
    EnergyBall selectByPrimaryKey(Integer id);
    int updateByExampleSelective(@Param("record") EnergyBall record, @Param("example") EnergyBallExample example);
    int updateByExample(@Param("record") EnergyBall record, @Param("example") EnergyBallExample example);
    int updateByPrimaryKeySelective(EnergyBall record);
    int updateByPrimaryKey(EnergyBall record);
}