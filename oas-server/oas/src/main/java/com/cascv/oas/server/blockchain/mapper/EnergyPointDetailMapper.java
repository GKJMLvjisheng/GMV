package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.EnergyPointDetail;
import org.springframework.stereotype.Component;

@Component
public interface EnergyPointDetailMapper {
  Integer insertSelective(EnergyPointDetail energyPointDetail);
  List<EnergyPointDetail> selectByUuid(String uuid);
  List<EnergyPointDetail> selectByOffset(@Param("userUuid") String userUuid, @Param("offset") Integer offfset, @Param("limit")String limit);
  List<EnergyPointDetail> selectActivityByDate(
        @Param("userUuid") String userUuid, 
        @Param("activity") Integer activity, 
        @Param("dateFrom") String dateFrom, 
        @Param("dateEnd") String dateEnd);
  Integer deleteByUserUuid(String userUuid);
  Integer deleteByUuid(Integer uuid);
}
