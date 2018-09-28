package com.cascv.oas.server.blockchain.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.wrapper.UserWalletTotalTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTradeRecordInfo;;

public interface UserWalletTradeRecordMapper {
	
	List<UserWalletTradeRecordInfo> selectAllTradeRecord();
	List<UserWalletTotalTradeRecordInfo> selectAllOutTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<UserWalletTotalTradeRecordInfo> selectAllInTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
}
