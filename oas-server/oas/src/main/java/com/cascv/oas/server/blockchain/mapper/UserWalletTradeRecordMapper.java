package com.cascv.oas.server.blockchain.mapper;
import java.util.List;

import com.cascv.oas.server.blockchain.wrapper.UserWalletTradeRecordInfo;;

public interface UserWalletTradeRecordMapper {
	
	List<UserWalletTradeRecordInfo> selectAllTradeRecord();
	
}
