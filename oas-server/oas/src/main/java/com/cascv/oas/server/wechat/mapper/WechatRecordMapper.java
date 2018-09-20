package com.cascv.oas.server.wechat.mapper;

import org.springframework.stereotype.Component;
import com.cascv.oas.server.energy.model.EnergyQuestion;


@Component
public interface WechatRecordMapper {
	 Integer insertRecord(EnergyQuestion energyQuestion);	 
}
