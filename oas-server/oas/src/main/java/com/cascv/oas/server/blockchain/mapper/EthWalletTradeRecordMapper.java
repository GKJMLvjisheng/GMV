package com.cascv.oas.server.blockchain.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.wrapper.EthWalletTotalTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTradeRecordInfo;;

public interface EthWalletTradeRecordMapper {
	
	List<EthWalletTradeRecordInfo> selectAllTradeRecord();
	List<EthWalletTotalTradeRecordInfo> selectAllInTotalTradeRecord();
	List<EthWalletTotalTradeRecordInfo> selectAllOutTotalTradeRecord();
	List<EthWalletTotalTradeRecordInfo> selectAllOutTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<EthWalletTotalTradeRecordInfo> selectAllInTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
}
