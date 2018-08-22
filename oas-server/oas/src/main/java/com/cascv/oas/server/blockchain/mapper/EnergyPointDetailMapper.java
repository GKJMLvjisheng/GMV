package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.EnergyPointDetail;

public interface EnergyPointDetailMapper {
  Integer insertSelective(EnergyPointDetail energyPointDetail);
  List<EnergyPointDetail> selectByUserUuid(String userUuid);
  List<EnergyPointDetail> selectByDateRange(@Param("userUuid") String userUuid, @Param("fromDate") String fromDate, @Param("toDate")String toDate);
  Integer deleteByUserUuid(String userUuid);
  Integer deleteByUuid(Integer uuid);
}
