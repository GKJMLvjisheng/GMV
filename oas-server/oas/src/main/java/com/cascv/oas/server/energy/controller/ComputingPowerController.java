package com.cascv.oas.server.energy.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.service.PowerService;
import com.cascv.oas.server.energy.vo.EnergyOfficialAccountResult;
import com.cascv.oas.server.energy.vo.EnergyPowerChangeDetail;
import com.cascv.oas.server.energy.vo.InviteUserInfo;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.wechat.Service.WechatService;
import com.cascv.oas.server.wechat.vo.IdenCodeDomain;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/computingPower")
@Slf4j
public class ComputingPowerController {
	
	@Autowired
    private EnergyService energyService;
	@Autowired
	private UserService userService;
	
    private PowerService powerService;
	@Autowired
	private WechatService wechatService;
	Set<String> userNameSet=new HashSet();
	
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

	@PostMapping(value = "/promotePowerByOfficialAccount")
    @ResponseBody
    public ResponseEntity<?> promotePowerByOfficialAccount(@RequestBody IdenCodeDomain code){
	 		   
		   Map<String,Object> userInfo=wechatService.inquireUserInfo();
		   String name=ShiroUtils.getUser().getName();
//		   Integer identifyCode=userService.findUserByName(name).getIdentifyCode();		   
		   log.info(userNameSet.toString());
		   String idenCode=code.getIdenCode();
		   if(code!=null&&userInfo.get(name)!=null){
			   if(userInfo.get(name).equals(idenCode)){
				   if(!userNameSet.contains(name)){
				   log.info("验证成功,提升算力！");
				   log.info(userNameSet.toString());
			        String userUuid = ShiroUtils.getUserUuid();
			        EnergyOfficialAccountResult energyOAResult = new EnergyOfficialAccountResult();
			        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
			            //powerService.saveOAEnergyBall(userUuid,now);
			            powerService.saveOAEnergyRecord(userUuid,now);
			            energyOAResult = powerService.getOAEnergy();			      
			            powerService.updateOAEnergyWallet(userUuid);
			            //一个验证码只能使用一次
			            log.info(name);
			            userNameSet.add(name);
			            return new ResponseEntity.Builder<Integer>()
			                    .setData(0)
			                    .setErrorCode(ErrorCode.SUCCESS)
				                .build();
			   }else {
				   log.info("每个用户只能使用一次验证码来提升算力!");
				   return new ResponseEntity.Builder<Integer>()
		                    .setData(2)
		                    .setErrorCode(ErrorCode.GENERAL_ERROR)
		                    .build();
			        }
			   }
			   else {
				   log.info("验证码输入错误!");
				   return new ResponseEntity.Builder<Integer>()
		                    .setData(1)
		                    .setErrorCode(ErrorCode.GENERAL_ERROR)
		                    .build();
			        }
			     }
		   else {
			   log.info("用户名不存在！");
			   return new ResponseEntity.Builder<Integer>()
	                    .setData(1)
	                    .setErrorCode(ErrorCode.GENERAL_ERROR)
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
        
        List<EnergyPowerChangeDetail> energyPowerChangeDetailList = energyService
        		.searchEnergyPowerChange(ShiroUtils.getUserUuid(), offset, limit);
     
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
	
	
	@PostMapping(value = "/addQuestion")
    @ResponseBody
	public ResponseEntity<?> addQuestion(){
		
		
		
		return null;
		
		
	}

}
