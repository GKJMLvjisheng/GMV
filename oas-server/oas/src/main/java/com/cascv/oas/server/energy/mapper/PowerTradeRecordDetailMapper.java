package com.cascv.oas.server.energy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.energy.vo.PowerTradeRecordDetail;

@Component
public interface PowerTradeRecordDetailMapper {

	List<PowerTradeRecordDetail> selectPowerTradeRecordBySearchValue(@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countPowerTradeRecordBySearchValue(@Param("searchValue") String searchValue);
	
}
