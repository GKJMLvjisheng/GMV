package com.cascv.oas.server.energy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.service.PowerService;
import com.cascv.oas.server.energy.vo.EnergyOfficialAccountResult;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.wechat.Service.WechatService;
import com.cascv.oas.server.wechat.vo.IdenCodeDomain;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/energyPower")
@Slf4j
public class ComputingPowerController {
	
	@Autowired
    private EnergyService energyService;
	@Autowired
    private PowerService powerService;
	@Autowired
	private WechatService wechatService;
	
	@PostMapping(value = "/inquirePower")
    @ResponseBody
    public ResponseEntity<?> inquirePower() {
        EnergyWallet energyWallet = energyService.findByUserUuid(ShiroUtils.getUserUuid());
        if (energyWallet != null) {
            return new ResponseEntity.Builder<Integer>()
                    .setData(energyWallet.getPower().intValue())
                    .setErrorCode(ErrorCode.SUCCESS)
                    .build();
        } else {
            return new ResponseEntity.Builder<Integer>()
                    .setData(0)
                    .setErrorCode(ErrorCode.NO_ENERGY_POINT_ACCOUNT)
                    .build();
        }
    }
	@PostMapping(value = "/promotePowerByOfficialAccount")
    @ResponseBody
    public ResponseEntity<?> promotePowerByOfficialAccount(@RequestBody IdenCodeDomain code) {
		  
		   Map<String,Object> userInfo=wechatService.inquireUserInfo();
		   String idenCode=code.getIdenCode();
		   String name= ShiroUtils.getUser().getName();
		   if(code!=null&&userInfo.get(name)!=null){
			   if(userInfo.get(name).equals(idenCode)){
				   log.info("验证成功,提升算力！");
			        String userUuid = ShiroUtils.getUserUuid();
			        EnergyOfficialAccountResult energyOAResult = new EnergyOfficialAccountResult();
			        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
			            powerService.saveOAEnergyBall(userUuid);
			            powerService.saveOAEnergyRecord(userUuid,now);
			            energyOAResult = powerService.getOAEnergy();			      
			            powerService.updateOAEnergyWallet(userUuid);	                       			   			   			   			   				   
			            return new ResponseEntity.Builder<EnergyOfficialAccountResult>()
			                    .setData(energyOAResult)
			                    .setErrorCode(ErrorCode.SUCCESS)
				                .build();
			   }
			   else {
				   log.info("验证失败！");
				   return new ResponseEntity.Builder<Integer>()
		                    .setData(0)
		                    .setErrorCode(ErrorCode.GENERAL_ERROR)
		                    .build();
			        }
			     }
		   else {
			   log.info("请输入验证码...");
			   return new ResponseEntity.Builder<Integer>()
	                    .setData(0)
	                    .setErrorCode(ErrorCode.GENERAL_ERROR)
	                    .build(); 
		   }
		 }		
    }
