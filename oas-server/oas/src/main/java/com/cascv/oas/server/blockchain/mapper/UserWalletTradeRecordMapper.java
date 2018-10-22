package com.cascv.oas.server.blockchain.mapper;
import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTradeRecordInfo;;

public interface UserWalletTradeRecordMapper {
	
	List<UserWalletTradeRecordInfo> selectAllTradeRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	List<WalletTotalTradeRecordInfo> selectAllOutTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	List<WalletTotalTradeRecordInfo> selectAllInTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	List<WalletTotalTradeRecordInfo> selectAllUserBalanceRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	
	BigDecimal selectUserPointToOas(@Param("userUuid") String userUuid,@Param("startTime") String startTime,@Param("endTime") String endTime);
}
