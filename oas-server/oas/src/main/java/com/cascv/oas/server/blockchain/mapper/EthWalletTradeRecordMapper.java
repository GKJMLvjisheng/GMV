package com.cascv.oas.server.blockchain.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.wrapper.EthWalletTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;;

public interface EthWalletTradeRecordMapper {
	
	List<EthWalletTradeRecordInfo> selectAllTradeRecord();
	List<WalletTotalTradeRecordInfo> selectAllOutTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<WalletTotalTradeRecordInfo> selectAllInTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
}
