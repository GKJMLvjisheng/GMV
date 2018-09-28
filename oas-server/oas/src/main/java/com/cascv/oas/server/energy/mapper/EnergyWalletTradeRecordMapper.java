package com.cascv.oas.server.energy.mapper;
import java.util.List;

import com.cascv.oas.server.energy.vo.EnergyWalletTradeRecordInfo;;

public interface EnergyWalletTradeRecordMapper {
	
	List<EnergyWalletTradeRecordInfo> selectAllTradeRecord();
	
}
