package com.cascv.oas.server.blockchain.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.wrapper.EthWalletTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;;

public interface EthWalletTradeRecordMapper {
	
	List<EthWalletTradeRecordInfo> selectAllTradeRecord(@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByTradeRecord();
	List<WalletTotalTradeRecordInfo> selectAllOutTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByOutTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
	List<WalletTotalTradeRecordInfo> selectAllInTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countByInTotalTradeRecord(@Param("startTime") String startTime,@Param("endTime") String endTime);
}
