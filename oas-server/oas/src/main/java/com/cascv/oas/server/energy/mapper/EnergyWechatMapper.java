package com.cascv.oas.server.energy.mapper;


import org.springframework.stereotype.Component;
import com.cascv.oas.server.energy.model.EnergyWechatModel;

@Component
public interface EnergyWechatMapper {
	 Integer insertWechatRecord(EnergyWechatModel energyWechatModel);
	 EnergyWechatModel findWechatRecordByUserUuid(String userUuid);
	 EnergyWechatModel findWechatRecordByOpenid(String wechatOpenid);
}
