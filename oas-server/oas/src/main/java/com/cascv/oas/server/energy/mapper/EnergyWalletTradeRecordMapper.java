package com.cascv.oas.server.energy.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.energy.vo.EnergyWalletBalanceRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletPointRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletTradeRecordInfo;;
@Component
public interface EnergyWalletTradeRecordMapper {
	
	List<EnergyWalletTradeRecordInfo> selectAllTradeRecordBySearchValue(@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByEnergyWalletTradeRecordBySearchValue(@Param("searchValue") String searchValue);
	
	List<EnergyWalletBalanceRecordInfo> selectAllEnergyWalletBalanceRecordBySearchValue(@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByEnergyWalletBalanceRecordBySearchValue(@Param("searchValue") String searchValue);
	
	List<EnergyWalletPointRecordInfo> selectAllInTotalPointTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByInTotalPointTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("searchValue") String searchValue);
	
	List<EnergyWalletPointRecordInfo> selectAllOutTotalPointTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByOutTotalPointTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("searchValue") String searchValue);
}
