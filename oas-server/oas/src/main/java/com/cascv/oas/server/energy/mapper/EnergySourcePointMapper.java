package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergySourcePoint;
import com.cascv.oas.server.energy.model.EnergySourcePointExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnergySourcePointMapper {
    long countByExample(EnergySourcePointExample example);
    int deleteByExample(EnergySourcePointExample example);
    int deleteByPrimaryKey(Integer id);
    int insert(EnergySourcePoint record);
    int insertSelective(EnergySourcePoint record);
    List<EnergySourcePoint> selectByExample(EnergySourcePointExample example);
    EnergySourcePoint selectByPrimaryKey(Integer id);
    int updateByExampleSelective(@Param("record") EnergySourcePoint record, @Param("example") EnergySourcePointExample example);
    int updateByExample(@Param("record") EnergySourcePoint record, @Param("example") EnergySourcePointExample example);
    int updateByPrimaryKeySelective(EnergySourcePoint record);
    int updateByPrimaryKey(EnergySourcePoint record);
}