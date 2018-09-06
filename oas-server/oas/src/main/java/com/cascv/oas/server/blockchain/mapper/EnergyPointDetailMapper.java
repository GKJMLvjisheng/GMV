package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.energy.vo.EnergyChangeDetail;

@Component
public interface EnergyPointDetailMapper {
  Integer insertSelective(EnergyChangeDetail energyPointDetail);
  List<EnergyChangeDetail> selectByUuid(String uuid);
  List<EnergyChangeDetail> selectByOffset(@Param("userUuid") String userUuid, @Param("offset") Integer offfset, @Param("limit")String limit);
  List<EnergyChangeDetail> selectActivityByDate(
        @Param("userUuid") String userUuid, 
        @Param("activity") Integer activity, 
        @Param("dateFrom") String dateFrom, 
        @Param("dateEnd") String dateEnd);
  Integer deleteByUserUuid(String userUuid);
  Integer deleteByUuid(Integer uuid);
}
