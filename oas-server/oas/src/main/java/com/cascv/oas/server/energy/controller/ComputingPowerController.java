package com.cascv.oas.server.energy.controller;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.EnergyPowerChangeDetail;
import com.cascv.oas.server.energy.vo.InviteUserInfo;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/computingPower")
@Slf4j
public class ComputingPowerController {
	
	@Autowired
    private EnergyService energyService;
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/promotePowerByFriendsShared")
    @ResponseBody
    public ResponseEntity<?> promotePowerByFriendsShared(@RequestBody InviteUserInfo inviteUserInfo) {
		UserModel userModel =new UserModel();
		Integer inviteFrom=inviteUserInfo.getInviteFrom();
		//查询邀请用户的上一级userModel
		userModel=userService.findUserByInviteCode(inviteFrom);
		
		if(inviteFrom!=0) {
			//查询邀请用户的上一级用户的Uuid
			String userUuid=userModel.getUuid();
			log.info("userUuid={}",userUuid);
			//插入上级用户的power变化
			energyService.saveCheckinEnergyBall(userUuid);
			
			
			
		}else {
			log.info("用户是自主注册用户，无人邀请");
		}
		
		
		return null;
		
	}
	
	@PostMapping(value = "/inqureInviteStatistical")
    @ResponseBody
    public ResponseEntity<?> inqureInviteStatistical() {
		
		return null;
		
	}
	
	
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
	
	@PostMapping(value = "/inquirePowerDetail")
    @ResponseBody
    public ResponseEntity<?> inquirePowerDetail(@RequestBody PageDomain<Integer> pageInfo) {
		Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer limit = pageSize;
        Integer offset;
 
        if (limit == null) {
          limit = 10;
        }
        
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
        
        List<EnergyPowerChangeDetail> energyPowerChangeDetailList = energyService.searchEnergyPowerChange(ShiroUtils.getUserUuid(), offset, limit);
        Integer count = energyService.countEnergyChange(ShiroUtils.getUserUuid());
        
        PageDomain<EnergyPowerChangeDetail> pageEnergyPowerDetail = new PageDomain<>();
        pageEnergyPowerDetail.setTotal(count);
        pageEnergyPowerDetail.setAsc("asc");
        pageEnergyPowerDetail.setOffset(offset);
        pageEnergyPowerDetail.setPageNum(pageNum);
        pageEnergyPowerDetail.setPageSize(pageSize);
        pageEnergyPowerDetail.setRows(energyPowerChangeDetailList);
        
		return new ResponseEntity.Builder<PageDomain<EnergyPowerChangeDetail>>()
				.setData(pageEnergyPowerDetail)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/backupsWallet")
    @ResponseBody
	public ResponseEntity<?> backupsWallet(){
		
		
		
		return null;
		
		
	}
	
	
	@PostMapping(value = "environmentalProtection")
    @ResponseBody
	public ResponseEntity<?> environmentalProtection(){
		
		
		
		return null;
		
		
	}

}
