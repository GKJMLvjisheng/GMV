package com.cascv.oas.server.energy.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.mapper.EnergyTopicMapper;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.model.EnergyTopicModel;
import com.cascv.oas.server.energy.model.EnergyUserTopicModel;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.service.PowerService;
import com.cascv.oas.server.energy.vo.ActivityResult;
import com.cascv.oas.server.energy.vo.ActivityResultList;
import com.cascv.oas.server.energy.vo.ChoiceResult;
import com.cascv.oas.server.energy.vo.EnergyOfficialAccountResult;
import com.cascv.oas.server.energy.vo.EnergyPowerChangeDetail;
import com.cascv.oas.server.energy.vo.QueryInvitePowerInfo;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.wechat.vo.IdenCodeDomain;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/computingPower")
@Slf4j
public class ComputingPowerController {
	
	@Autowired
    private UserService userService;
	@Autowired
    private EnergyService energyService;

	@Autowired
    private PowerService powerService;
	@Autowired
	private EnergyTopicMapper energyTopicMapper;
	@Autowired
	private EnergySourcePowerMapper energySourcePowerMapper;
	
    ActivityCompletionStatus activityCompletionStatus=new ActivityCompletionStatus();   
	
	@PostMapping(value = "/inquirePowerActivityStatus")
    @ResponseBody
    public ResponseEntity<?> inquirePowerActivityStatus(){
		List<ActivityResult> activityResult = powerService.searchActivityStatus(ShiroUtils.getUserUuid());
		ActivityResultList activityResultList = new ActivityResultList();
		activityResultList.setActivityResultList(activityResult);
		return new ResponseEntity.Builder<ActivityResultList>()
				.setData(activityResultList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
		
	@PostMapping(value = "/inqureInviteStatistical")
    @ResponseBody
    public ResponseEntity<?> inqureInviteStatistical() {
		UserModel userModel=ShiroUtils.getUser();
		QueryInvitePowerInfo queryInvitePowerInfo=new QueryInvitePowerInfo();
		Integer SumUserInvited,SumPowerPromoted;
		Integer inviteCode=userModel.getInviteCode();
		SumUserInvited=10;
		SumPowerPromoted=150;
		queryInvitePowerInfo.setSumUserInvited(SumUserInvited);
		queryInvitePowerInfo.setSumPowerPromoted(SumPowerPromoted);
		return new ResponseEntity.Builder<QueryInvitePowerInfo>()
                .setData(queryInvitePowerInfo)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
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

	@PostMapping(value = "/promotePowerByWechatAccount")
    @ResponseBody
    public ResponseEntity<?> promotePowerByOfficialAccount(@RequestBody IdenCodeDomain code){
	 		   
		    String name=ShiroUtils.getUser().getName();	   
		    String idenCode=code.getIdenCode();
		   	String userUuid=ShiroUtils.getUserUuid();
		   	try{
		   		if(energySourcePowerMapper.selectACSByUserUuid(userUuid)!=null) {
	        	activityCompletionStatus=energySourcePowerMapper.selectACSByUserUuid(userUuid);
	        	log.info("activityCompletionStatus is not null");
	          }else {
	        	  activityCompletionStatus=null;
	        	  log.info("next");
	                }
		   		}catch(Exception e) {
		   			log.info(e.getMessage());
		   			e.getStackTrace();
		   		}	   	
		   	
		   	UserModel userModel=new UserModel();
		   	userModel=userService.findUserByName(ShiroUtils.getUser().getName());
		   	log.info(idenCode);
		   if(code!=null&&activityCompletionStatus!=null){
			   if(userModel.getIdentifyCode().toString().equals(idenCode)){
				   if(activityCompletionStatus.getStatus()!=1){
				   log.info("验证成功,提升算力！");
			        EnergyOfficialAccountResult energyOAResult = new EnergyOfficialAccountResult();
			        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
			            //powerService.saveOAEnergyBall(userUuid,now);
			            powerService.saveOAEnergyRecord(userUuid,now);
			            energyOAResult = powerService.getOAEnergy();			      
			            powerService.updateOAEnergyWallet(userUuid);
			            activityCompletionStatus.setStatus(1);
			            activityCompletionStatus.setUserUuid(userUuid);
			            energySourcePowerMapper.updateStatus(activityCompletionStatus);
			            //一个验证码只能使用一次
			            log.info(name);
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
        
        List<EnergyPowerChangeDetail> energyPowerChangeDetailList = powerService
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
		
		String userUuid = ShiroUtils.getUserUuid();
		ErrorCode errorCode = ErrorCode.SUCCESS;
		
		if(powerService.isBackupsWallet(userUuid) == 0) {
			//do backupsWallet
			
			return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(errorCode).build();
			
		}else {
			errorCode = ErrorCode.IS_BACKUPS_WALLET;
			return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(errorCode).build();

		}		

	}
		
	@PostMapping(value = "/addTopic")
    @ResponseBody
	public ResponseEntity<?> addTopic(EnergyTopicModel energytopic){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		log.info("***Start***");
		EnergyTopicModel energyTopicModel = new EnergyTopicModel();	
		energyTopicModel.setQuestion(energytopic.getQuestion());
		energyTopicModel.setChoiceA(energytopic.getChoiceA());
		energyTopicModel.setChoiceB(energytopic.getChoiceB());
		energyTopicModel.setChoiceC(energytopic.getChoiceC());
		energyTopicModel.setChoiceRight(energytopic.getChoiceRight()); 
		energyTopicModel.setCreated(now);
		energyTopicMapper.insertTopic(energyTopicModel);
		  return new ResponseEntity.Builder<Integer>()
					.setData(0)
					.setErrorCode(ErrorCode.SUCCESS)
					.build();				
	}
	
	@PostMapping(value = "/deleteTopic")
    @ResponseBody
	public ResponseEntity<?> deleteTopic(@RequestBody EnergyTopicModel energytopic){
		Integer topicId=energytopic.getTopicId();
		log.info("topicId={}",topicId);
		
		energyTopicMapper.deleteTopic(topicId);
		
		return new ResponseEntity.Builder<Integer>()
				.setData(0).setErrorCode(ErrorCode.SUCCESS).build();
						
	}
	@PostMapping(value = "/updateTopic")
    @ResponseBody
	public ResponseEntity<?> updateTopic(EnergyTopicModel energytopic){
		log.info("--------start--------");
		log.info("topicId{}",energytopic.getTopicId());
		EnergyTopicModel energyTopicModel = new EnergyTopicModel();		
		energyTopicModel.setTopicId(energytopic.getTopicId());
		energyTopicModel.setQuestion(energytopic.getQuestion());
		energyTopicModel.setChoiceA(energytopic.getChoiceA());
		energyTopicModel.setChoiceB(energytopic.getChoiceB());
		energyTopicModel.setChoiceC(energytopic.getChoiceC());
		energyTopicModel.setChoiceRight(energytopic.getChoiceRight());  
		energyTopicMapper.updateTopic(energyTopicModel);
	    return new ResponseEntity.Builder<Integer>()
					.setData(0)
					.setErrorCode(ErrorCode.SUCCESS)
					.build();				
	}
	@PostMapping(value = "/selectAllTopic")
    @ResponseBody
	public ResponseEntity<?> selectAllTopic(){
		 Map<String,Object> info=new HashMap<>();
		  List<EnergyTopicModel> list=energyTopicMapper.selectAllTopic();
		  int length=list.size();
		  if(length>0) {
		     info.put("list", list);
		  }else
		  {
		    log.info("no news in mysql");
		  }
		    return new ResponseEntity.Builder<Map<String, Object>>()
		              .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
		  }
	/**
	 * 判断题目是否正确
	 * @param energyTopicModel
	 * @return
	 */
	@PostMapping(value = "/judgeChoice")
    @ResponseBody
	public ResponseEntity<?> judgeChoice(@RequestBody ChoiceResult choiceResult){
		//前端传回 topicId,topicChoiced
		Map<String,Object> info=new HashMap<>();
		EnergyUserTopicModel energyUserTopicModel =new EnergyUserTopicModel();
		Integer topicId=choiceResult.getTopicId();
		EnergyTopicModel energyTopicModel=energyTopicMapper.findTopicByTopicId(topicId);
        String choiceRight =energyTopicModel.getChoiceRight();        
        if(choiceRight.equals(choiceResult.getTopicChoiced())){
        	//将正确的记录存入数据库，答错的话不作处理
        	String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        	String userTopicUuid=UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT);
        	energyUserTopicModel.setTopicId(topicId);
        	String userUuid=ShiroUtils.getUserUuid();
        	energyUserTopicModel.setUserUuid(userUuid);
        	energyUserTopicModel.setUserTopicUuid(userTopicUuid);
        	energyUserTopicModel.setCreated(now);
        	energyTopicMapper.insertUserTopic(energyUserTopicModel);
    	    return new ResponseEntity.Builder<Integer>()
  	              .setData(0).setErrorCode(ErrorCode.SUCCESS).build();
        }else{
    	    return new ResponseEntity.Builder<Integer>()
  	              .setData(1).setErrorCode(ErrorCode.GENERAL_ERROR).build();
  		     }
        }
	
	/**
	 * 判断题目是否做过
	 * @param energyTopicModel
	 * @return
	 */
	@PostMapping(value = "/judgeTopicIsDone")
    @ResponseBody
	public ResponseEntity<?> judgeTopicIsDone(@RequestBody ChoiceResult choiceResult){
		Map<String,Object> info=new HashMap<>();
		Integer topicId=choiceResult.getTopicId();
		UserModel userModel=ShiroUtils.getUser();
		EnergyUserTopicModel energyUserTopicModel=new EnergyUserTopicModel();
		String UserUuid=userModel.getUuid();
		if(energyTopicMapper.findTopicByUserUuid(UserUuid)!=null){
			energyUserTopicModel= energyTopicMapper.findTopicByUserUuid(UserUuid);
			if(energyUserTopicModel.getTopicId()!=topicId){
				log.info("还没做过这道题");
			return new ResponseEntity.Builder<Integer>()
		              .setData(0).setErrorCode(ErrorCode.SUCCESS).build();
		    }else{
				log.info("已经做过这道题了");
				return new ResponseEntity.Builder<Integer>()
			              .setData(1).setErrorCode(ErrorCode.SUCCESS).build();
			     }			
	  }else{
			return new ResponseEntity.Builder<Integer>()
		              .setData(1).setErrorCode(ErrorCode.GENERAL_ERROR).build();
		   }	    
}
	/**
	 * 分页显示题目(已经做过的不显示)
	 * @param energyTopicModel
	 * @return
	 */
	@PostMapping(value = "/inquireTopicByPage")
    @ResponseBody
	public ResponseEntity<?> inquireTopicByPage(@RequestBody EnergyTopicModel energyTopicModel){
		Map<String,Object> info=new HashMap<>();
        
	    return new ResponseEntity.Builder<Map<String, Object>>()
	              .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
		  }
}
