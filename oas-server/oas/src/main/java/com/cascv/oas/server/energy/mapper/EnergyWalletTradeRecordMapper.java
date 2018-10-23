package com.cascv.oas.server.energy.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.energy.vo.EnergyWalletBalanceRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletPointRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletTradeRecordInfo;;

public interface EnergyWalletTradeRecordMapper {
	
	List<EnergyWalletTradeRecordInfo> selectAllTradeRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByEnergyWalletTradeRecord();
	List<EnergyWalletTradeRecordInfo> selectAllTradeRecordBySearchValue(@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByEnergyWalletTradeRecordBySearchValue(@Param("searchValue") String searchValue);
	
	List<EnergyWalletBalanceRecordInfo> selectAllEnergyWalletBalanceRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByEnergyWalletBalanceRecord();
	List<EnergyWalletBalanceRecordInfo> selectAllEnergyWalletBalanceRecordBySearchValue(@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByEnergyWalletBalanceRecordBySearchValue(@Param("searchValue") String searchValue);
	
	List<EnergyWalletPointRecordInfo> selectAllInTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByInTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<EnergyWalletPointRecordInfo> selectAllInTotalPointTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByInTotalPointTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("searchValue") String searchValue);
	
	List<EnergyWalletPointRecordInfo> selectAllOutTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByOutTotalPointTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<EnergyWalletPointRecordInfo> selectAllOutTotalPointTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByOutTotalPointTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("searchValue") String searchValue);
}
