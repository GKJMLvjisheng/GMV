package com.cascv.oas.server.blockchain.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTradeRecordInfo;;

public interface UserWalletTradeRecordMapper {
	
	List<UserWalletTradeRecordInfo> selectAllTradeRecord();
	List<WalletTotalTradeRecordInfo> selectAllOutTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<WalletTotalTradeRecordInfo> selectAllInTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<WalletTotalTradeRecordInfo> selectAllUserBalanceRecord();
}
