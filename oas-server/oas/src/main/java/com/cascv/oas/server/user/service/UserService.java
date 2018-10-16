package com.cascv.oas.server.user.service;


import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.user.model.UserIdentityCardModel;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.news.config.MediaServer;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.user.mapper.UserIdentityCardModelMapper;
import com.cascv.oas.server.user.mapper.UserModelMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
  @Autowired
  private UserModelMapper userModelMapper;
  
  @Autowired
  private UserIdentityCardModelMapper userIdentityCardModelMapper;
  @Autowired 
  private TimeZoneService timeZoneService;
  @Autowired
  private MediaServer mediaServer;
	
  public UserModel findUserByName(String name){
    UserModel userModel = userModelMapper.selectByName(name);
    return userModel;
  }

  public UserModel findUserByUuid(String uuid){
    UserModel userModel = userModelMapper.selectByUuid(uuid); 
    return userModel;
  }
  
  public UserModel findUserByInviteCode(Integer inviteCode) {
	  UserModel userModel = userModelMapper.selectByInviteCode(inviteCode);
	  return userModel;
  }
  
//  //随机产生六位邀请码，已判断重复
//  public String GenerateInviteCode() {
//	  long i = (int)((Math.random()*9+1)*10000000);
//	  String inviteCode = Long.toString(i, 36);
//	  if (findUserByInviteCode(inviteCode) == null) {
//		  return inviteCode;		  
//	  } else {
//		  return GenerateInviteCode();		  
//		  }	  
//  }
   
  public ErrorCode addUser(String uuid, UserModel userModel) {
	  log.info("111");
//	  log.info("userModel.getInviteCode()={}",userModel.getInviteCode());
//	  log.info("userModel.getInviteFrom()={}",userModel.getInviteFrom());
	  String s = userModel.getName();
	  if(s == "") {
      return ErrorCode.USERNAME_NULL;
    }
	  String password = userModel.getPassword();
    if(password == "") {
      return ErrorCode.PASSWORD_NULL;
    }

    UserModel searchUserModel = findUserByName(userModel.getName());
    if (searchUserModel != null) {
      return ErrorCode.USER_ALREADY_EXISTS;
    }
    if (userModel.getNickname() == null) {
      userModel.setNickname(userModel.getName());
    }
    
//    if (userModel.getInviteCode() == null) {
//        userModel.setInviteCode(userModel.getInviteCode());
//      }
//    
//    if (userModel.getInviteFrom() == null) {
//        userModel.setInviteFrom(userModel.getInviteFrom());
//      }
    userModel.setUuid(uuid);
    userModel.setSalt(DateUtils.dateTimeNow());
	  userModel.setPassword(new Md5Hash(userModel.getName() + password + userModel.getSalt()).toHex().toString());
	
    //产生邀请码       
//    String inviteCode = InviteCodeUtils.getFromUuid(uuid);
//    log.info("inviteCode {}", inviteCode);
//    userModel.setInviteCode(inviteCode);
    String now = DateUtils.getTime();
    
    userModel.setCreated(now);
    userModel.setUpdated(now);
    
    userModelMapper.insertUser(userModel);
    /**
     * @author Ming Yang
     * Date:20181012 
     * 注册用户添加用户的认证初始状态
     */
    log.info("*****start insert user identify card info*****");
    UserIdentityCardModel userIdentityCardModel=new UserIdentityCardModel();
    Integer verifyStatus=0;
    userIdentityCardModel.setUserName(s);
    userIdentityCardModel.setVerifyStatus(verifyStatus);
    userIdentityCardModel.setCreated(now);
    userIdentityCardModel.setUpdated(now);
    userIdentityCardModelMapper.insertUserIdentityCard(userIdentityCardModel);
    log.info("*****end user identify card info*****"); 
    log.info("inviteCode {}", userModel.getInviteCode());
    log.info("inviteFrom {}", userModel.getInviteFrom());
    return ErrorCode.SUCCESS;
  }
  
  public ErrorCode deleteUserByUuid(String uuid) {
    userModelMapper.deleteUser(uuid);
    return ErrorCode.SUCCESS;
  }
  
	/*
	 * Name:upadateUserInfo
	 * Author:lvjisheng
	 * Date:2018.09.03
	 */
	public Integer updateUser(UserModel userModel) {
		return userModelMapper.updateUserInfo(userModel);
	}
	
	/*
	 * Name:upadateUserInfo
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	public Integer updateUserProfile(UserModel userModel) {
		return userModelMapper.updateUserProfile(userModel);
	}
	
	/*
	 * Name:resetMobile
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	public Integer resetMobileByName(UserModel userModel) {
		return userModelMapper.resetMobileByName(userModel);
	}	
	
	/*
	 * Name:resetEmail
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	public Integer resetEmailByName(UserModel userModel) {
		return userModelMapper.resetEmailByName(userModel);
	}	
	

	public List<String> selectUserMail(String email){
		return userModelMapper.selectUserMail(email);
	}	

	public List<String> selectUserMobile(String mobile){
		return userModelMapper.selectUserMobile(mobile);
	}
	
	/*
	 * findUserByInviteFrom
	 * Author:yangming
	 * Date:2018.09.19
	 */	
	public UserModel findUserByInviteFrom(Integer inviteFrom) {
		UserModel userModel = userModelMapper.selectByInviteFrom(inviteFrom);
		return userModel;
	  }

	//验证码
	public Integer updateIdentifyCode(UserModel userModel) {
		return userModelMapper.updateIdentifyCode(userModel);
	}
	
	
	public String findUserByMobile(String mobile) {
		return userModelMapper.findUserByMobile(mobile);
	}
	
	public Integer updateUserPassworde(UserModel userModel) {
		return userModelMapper.updateUserPassworde(userModel);
	}
	
	/**
	 * @author Ming Yang
	 * Date:20181011
	 * 选出用户所有身份认证信息
	 */
	public List<UserIdentityCardModel> selectAllUserIdentityCard(){
		List<UserIdentityCardModel> userIdentityCardModelList=userIdentityCardModelMapper.selectAllUserIdentityCard();
		  for (UserIdentityCardModel userIdentityCardModel : userIdentityCardModelList) {
			    String frontOfPhoto = mediaServer.getImageHost() + userIdentityCardModel.getFrontOfPhoto();
			    String backOfPhoto = mediaServer.getImageHost() + userIdentityCardModel.getBackOfPhoto();
			    String holdInHand = mediaServer.getImageHost() + userIdentityCardModel.getHoldInHand();
			    String srcFormater="yyyy-MM-dd HH:mm:ss";
				String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, userIdentityCardModel.getCreated(), dstFormater, dstTimeZoneId);
				log.info("newCreated={}",created);
			    userIdentityCardModel.setFrontOfPhoto(frontOfPhoto);
			    userIdentityCardModel.setBackOfPhoto(backOfPhoto);
			    userIdentityCardModel.setHoldInHand(holdInHand);
			    userIdentityCardModel.setCreated(created);
			  }
		return userIdentityCardModelList;
	}
	
	/**
	 * @author Ming Yang
	 * 根据账户名选出用户的身份认证信息
	 */
	public List<UserIdentityCardModel> selectUserIdentityByUserName(String userName) {
		List<UserIdentityCardModel> userIdentityCardModelList=userIdentityCardModelMapper.selectUserIdentityByUserName(userName);
		 String frontOfPhoto = mediaServer.getImageHost() + userIdentityCardModelList.get(0).getFrontOfPhoto();
		 String backOfPhoto = mediaServer.getImageHost() + userIdentityCardModelList.get(0).getBackOfPhoto();
		 String holdInHand = mediaServer.getImageHost() + userIdentityCardModelList.get(0).getHoldInHand();
		 userIdentityCardModelList.get(0).setFrontOfPhoto(frontOfPhoto);
		 userIdentityCardModelList.get(0).setBackOfPhoto(backOfPhoto);
		 userIdentityCardModelList.get(0).setHoldInHand(holdInHand);
		return userIdentityCardModelList;
	}
	
	public List<String> selectIdentityNumber(String mobile){
		return userModelMapper.selectUserMobile(mobile);
	}
}



