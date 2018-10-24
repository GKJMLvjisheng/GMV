package com.cascv.oas.server.blockchain.mapper;
import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTradeRecordInfo;;

public interface UserWalletTradeRecordMapper {
	

	List<UserWalletTradeRecordInfo> selectAllTradeRecordBySearchValue(@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByTradeRecordBySearchValue(@Param("searchValue") String searchValue);
	
	List<WalletTotalTradeRecordInfo> selectAllOutTotalTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByOutTotalTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("searchValue") String searchValue);
	
	List<WalletTotalTradeRecordInfo> selectAllInTotalTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByInTotalTradeRecordBySearchValue(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("searchValue") String searchValue);
	
	List<WalletTotalTradeRecordInfo> selectAllUserBalanceRecordBySearchValue(@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countByUserBalanceRecordBySearchValue(@Param("searchValue") String searchValue);
	
	BigDecimal selectUserPointToOas(@Param("userUuid") String userUuid,@Param("startTime") String startTime,@Param("endTime") String endTime);
}
