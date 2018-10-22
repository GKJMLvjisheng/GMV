package com.cascv.oas.server.energy.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.energy.vo.EnergyWalletBalanceRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletPointRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletTradeRecordInfo;;

public interface EnergyWalletTradeRecordMapper {
	
	List<EnergyWalletTradeRecordInfo> selectAllTradeRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByEnergyWalletTradeRecord();
	List<EnergyWalletBalanceRecordInfo> selectAllEnergyWalletBalanceRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByEnergyWalletBalanceRecord();
	List<EnergyWalletPointRecordInfo> selectAllInTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByInTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<EnergyWalletPointRecordInfo> selectAllOutTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByOutTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
}
