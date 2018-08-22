package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.EnergyPointDetail;

public interface EnergyPointDetailMapper {
  Integer insertSelective(EnergyPointDetail energyPointDetail);
  List<EnergyPointDetail> selectByUserId(Integer userId);
  List<EnergyPointDetail> selectByDateRange(@Param("userId") Integer userId, @Param("fromDate") String fromDate, @Param("toDate")String toDate);
  Integer deleteByUserId(Integer userId);
  Integer deleteByUuid(Integer uuid);
}
