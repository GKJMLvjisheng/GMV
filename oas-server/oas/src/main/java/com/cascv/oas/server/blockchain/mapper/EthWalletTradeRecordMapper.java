package com.cascv.oas.server.blockchain.mapper;
import java.util.List;

import com.cascv.oas.server.blockchain.wrapper.EthWalletTotalTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTradeRecordInfo;;

public interface EthWalletTradeRecordMapper {
	
	List<EthWalletTradeRecordInfo> selectAllTradeRecord();
	List<EthWalletTotalTradeRecordInfo> selectAllInTotalTradeRecord();
	List<EthWalletTotalTradeRecordInfo> selectAllOutTotalTradeRecord();
	
}
