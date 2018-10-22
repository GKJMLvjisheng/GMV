package com.cascv.oas.server.energy.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.energy.vo.EnergyWalletBalanceRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletPointRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletTradeRecordInfo;;

public interface EnergyWalletTradeRecordMapper {
	
	List<EnergyWalletTradeRecordInfo> selectAllTradeRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	List<EnergyWalletBalanceRecordInfo> selectAllEnergyWalletBalanceRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	List<EnergyWalletPointRecordInfo> selectAllInTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	List<EnergyWalletPointRecordInfo> selectAllOutTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
}
