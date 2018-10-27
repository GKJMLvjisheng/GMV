package com.cascv.oas.server.user.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.FutureTask;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.SystemUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.sns.model.PublishResult;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.service.EnergyWalletService;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.log.annotation.WriteLog;
import com.cascv.oas.server.news.config.MediaServer;
import com.cascv.oas.server.shiro.BaseShiroController;
import com.cascv.oas.server.user.mapper.UserFacilityMapper;
import com.cascv.oas.server.user.mapper.UserIdentityCardModelMapper;
import com.cascv.oas.server.user.mapper.UserModelMapper;
import com.cascv.oas.server.user.mapper.UserRoleModelMapper;
import com.cascv.oas.server.user.model.MailInfo;
import com.cascv.oas.server.user.model.UserFacility;
import com.cascv.oas.server.user.model.UserIdentityCardModel;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.model.UserRole;
import com.cascv.oas.server.user.service.MessageService;
import com.cascv.oas.server.user.service.RoleService;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.user.wrapper.AuthCode;
import com.cascv.oas.server.user.wrapper.IMEIModel;
import com.cascv.oas.server.user.wrapper.LoginResult;
import com.cascv.oas.server.user.wrapper.LoginVo;
import com.cascv.oas.server.user.wrapper.MobileModel;
import com.cascv.oas.server.user.wrapper.RegisterConfirm;
import com.cascv.oas.server.user.wrapper.RegisterResult;
import com.cascv.oas.server.user.wrapper.updateUserInfo;
import com.cascv.oas.server.user.wrapper.UserDetailModel;
import com.cascv.oas.server.user.wrapper.UserStatus;
import com.cascv.oas.server.utils.SendMailUtils;
import com.cascv.oas.server.utils.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")

public class UserController extends BaseShiroController{
  @Value("${mail.mailBean.rootMail}")
  private String rootMail;
  @Value("${mail.mailBean.password}")
  private String mailPassword;
  @Value("${mail.mailBean.name}")
  private String mailName;
  @Value("${mail.mailBean.subject}")
  private String subject;
  @Autowired
  private UserService userService;
  @Autowired
  private MediaServer mediaServer;
//  @Autowired
//  private MailBean mailBean;
  @Autowired
  private RoleService roleService;
  @Autowired
  private EthWalletService ethWalletService;	
  @Autowired
  private UserWalletService userWalletService;
  @Autowired
  private EnergyWalletService energyPointService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private UserRoleModelMapper userRoleModelMapper;
  @Autowired
  private UserFacilityMapper userFacilityMapper;
  @Autowired
  private UserModelMapper userModelMapper;
  @Autowired
  private UserIdentityCardModelMapper userIdentityCardModelMapper;
   
  String SYSTEM_USER_HOME=SystemUtils.USER_HOME;
  String UPLOADED_FOLDER =SYSTEM_USER_HOME+File.separator+"Temp"+File.separator+"Image" + File.separator+"profile"+File.separator;	
  String IDENTITY_UPLOADED =SYSTEM_USER_HOME+File.separator+"Temp"+File.separator+"Image" + File.separator+"identityCard"+File.separator;	
  String vcode="";
     
	@ApiOperation(value="Login", notes="")
	//@RequiresRoles("admin")
	@PostMapping(value="/login")
	@ResponseBody
	@WriteLog(value="Login")
	public ResponseEntity<?> userLogin(@RequestBody LoginVo loginVo,HttpServletRequest request) {
		
		//获取请求头，来判断是来自web的请求还是移动端的请求
		String userAgent=request.getHeader("user-agent");
		log.info("user-agent={}",userAgent);		
		log.info("authentication name {}, password {}", loginVo.getName(), loginVo.getPassword());
		Boolean rememberMe = loginVo.getRememberMe() == null ? false : loginVo.getRememberMe();
		UsernamePasswordToken token = new UsernamePasswordToken(loginVo.getName(), loginVo.getPassword(), rememberMe);
        LoginResult loginResult = new LoginResult();
	    Subject subject = SecurityUtils.getSubject();
      try {
          subject.login(token);
          loginResult.setToken(ShiroUtils.getSessionId());
          //设置头像
          UserModel userModel=new UserModel();
    	  String fullLink = mediaServer.getImageHost() + ShiroUtils.getUser().getProfile();
          userModel=ShiroUtils.getUser();
          userModel.setProfile(fullLink);         
          ShiroUtils.setUser(userModel);
          
          /**
           * 判断IMEI是否相匹配
           */
          String IMEIOri=userService.findUserByName(loginVo.getName()).getIMEI();
          String IMEINew=loginVo.getIMEI();
          String uuid=ShiroUtils.getUser().getUuid();
          Set<String> roles=roleService.getRolesByUserUuid(uuid);
          List<String>  roleList =new ArrayList<>(roles);
          log.info("roles={}",roles);
          
         Integer status=userService.findUserByName(loginVo.getName()).getStatus();
         log.info("if android={}",userAgent.indexOf("Windows")==-1);
		 UserFacility userFacility=new UserFacility();
		 userFacility.setUuid(ShiroUtils.getUser().getUuid());
		 userFacility.setIMEI(IMEINew);
         if(status==0)
        	 throw new AuthenticationException();
         //判断是否是移动端登录
         if(userAgent.indexOf("Windows")==-1){
        	 log.info("this is android!");
        	 log.info(roleList.get(0));
        	 switch(roleList.get(0)){
	        	 case "系统账号":
	        		 log.info("this is 系统账号");
	        		 throw new AuthenticationException();
	        	 case "正常账号":
	        		 log.info("this is 正常账号");
	        		 if(IMEIOri==null) {
                         if(userFacilityMapper.inquireUserFacilityByIMEI(IMEINew)!=null)
                        	 throw new AuthenticationException();
                         else{
	        				 UserFacility userNewFacility=new UserFacility();
	    				     userNewFacility.setIMEI(loginVo.getIMEI());
	    				     userNewFacility.setUuid(uuid);
	    				     userNewFacility.setCreated(DateUtils.getTime());
	                         userFacilityMapper.insertUserFacility(userFacility);
		  	  	        	 userModel.setIMEI(loginVo.getIMEI());	        	 
			  	        	 userModelMapper.updateIMEI(userModel);
       			          }
	  	        	 }	        		
	        		 else{
		        			 if(IMEIOri.equals(IMEINew))
		        			 {    
		        				  if(userFacilityMapper.inquireUserFacilityByIMEI(IMEINew)!=null&&userFacilityMapper.inquireUserFacilityByModel(userFacility)==null)
		        					  throw new AuthenticationException();
		        				  else if(userFacilityMapper.inquireUserFacilityByIMEI(IMEINew)!=null){
			        				 UserFacility userNewFacility=new UserFacility();
			    				     userNewFacility.setIMEI(loginVo.getIMEI());
			    				     userNewFacility.setUuid(uuid);
			    				     userNewFacility.setCreated(DateUtils.getTime());
			                         userFacilityMapper.insertUserFacility(userFacility);
		        			      }          
	        			     }
	        			 else 
	        				 throw new AuthenticationException();	        			 
	        		    }
	        	     break;
	        	 case "测试账号":
	        		 log.info("this is 测试账号");
	        		 /**
		        	   userModel.setIMEI(loginVo.getIMEI());	        	 
		        	   userModelMapper.updateIMEI(userModel);
		        	   **/
	        	     break;
	        	 default:
	        		 log.info("default");
	        		 break;
        	 }
         } 
         
          //普通用户无法在web端登录
          else if(userAgent.indexOf("Windows")!=-1&&!roles.contains("系统账号"))
        	       throw new AuthenticationException();
         
          log.info("this is PC!");       
          loginResult.fromUserModel(ShiroUtils.getUser());
          return new ResponseEntity.Builder<LoginResult>()
              .setData(loginResult).setErrorCode(ErrorCode.SUCCESS)
              .build();
      } catch (AuthenticationException e)  {
          return new ResponseEntity.Builder<LoginResult>()
                .setData(loginResult)
                .setErrorCode(ErrorCode.LOGIN_FAILED).build();
      }
	}
	
	// Register
	@ApiOperation(value="Register", notes="")
	@PostMapping(value="/register")
	@ResponseBody
	@Transactional
	@WriteLog(value="Register")
	public ResponseEntity<?> register(@RequestBody UserModel userModel){
	  
	  String password = userModel.getPassword();
		RegisterResult registerResult = new RegisterResult();
		String uuid = UuidUtils.getPrefixUUID(UuidPrefix.USER_MODEL);
		EthWallet ethHdWallet = ethWalletService.create(uuid, password);
		userWalletService.create(uuid);
		energyPointService.create(uuid);
		//给用户赋予默认角色(正常账号roleId为2)
		
		  UserRole userRole=new UserRole();
		  userRole.setUuid(uuid);
		  userRole.setRoleId(2);
		  String now =DateUtils.getTime();
		  userRole.setRolePriority(1);

		  userRole.setCreated(now);		  		  
		  userRoleModelMapper.insertUserRole(userRole);  
					
		  
		ErrorCode ret = userService.addUser(uuid, userModel);
//		log.info("inviteCode {}", userModel.getInviteCode());
  	if (ret.getCode() == ErrorCode.SUCCESS.getCode()) {
  	  registerResult.setMnemonicList(EthWalletService.fromEncryptedMnemonicList(ethHdWallet.getMnemonicList()));
  	  registerResult.setUuid(userModel.getUuid());
  	  ret = ErrorCode.SUCCESS;
  	}
  	return new ResponseEntity.Builder<RegisterResult>()
		      .setData(registerResult).setErrorCode(ret).build();
	}

	// Destroy
	@ApiOperation(value="Destroy", notes="")
	@PostMapping(value="/destroy")
	@ResponseBody
	@Transactional
	@WriteLog(value="Destroy")
	public ResponseEntity<?> destroy() {
		UserModel userModel = ShiroUtils.getUser();
		String uuid = userModel.getUuid();
		ethWalletService.destroy(uuid);
		userWalletService.destroy(uuid);
		energyPointService.destroy(uuid);
		userService.deleteUserByUuid(uuid);
		return new ResponseEntity.Builder<Integer>()
					.setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	}

  @PostMapping(value="/registerConfirm")
  @ResponseBody
  @WriteLog(value="RegisterConfirm")
  public ResponseEntity<?> registerConfirm(@RequestBody RegisterConfirm registerConfirm) {
	  
	  UserModel userModel = userService.findUserByUuid(registerConfirm.getUuid());
	    if (userModel != null && registerConfirm.getCode() != null && registerConfirm.getCode() != 0) {
				String uuid = userModel.getUuid();
				System.out.println(uuid);
				ethWalletService.destroy(uuid);
				userWalletService.destroy(uuid);
				energyPointService.destroy(uuid);
	      userService.deleteUserByUuid(uuid);
	    }
	    return new ResponseEntity.Builder<Integer>()
	          .setData(0)
	          .setErrorCode(ErrorCode.SUCCESS)
	          .build();
  }
	  
//	  //根据前端返回uuid找到新注册用户
//	  UserModel userModel = userService.findUserByUuid(registerConfirm.getUuid());
//	  
//    if (userModel != null && registerConfirm.getCode() != null && registerConfirm.getCode() != 0) {
//    		//用户注册失败
//			String uuid = userModel.getUuid();
//			System.out.println(uuid);
//			ethWalletService.destroy(uuid);
//			userWalletService.destroy(uuid);
//			energyPointService.destroy(uuid);
//            userService.deleteUserByUuid(uuid);
//            //注册未完成
//			return new ResponseEntity.Builder<Integer>()
//	                   .setData(2)
//	                   .setErrorCode(ErrorCode.GENERAL_ERROR)
//	                   .build();
//    }else {
//    		//用户注册成功
//    	    Integer inviteFrom=userModel.getInviteFrom();
//    	    log.info("inviteFrom={}",inviteFrom);
//    	    if(inviteFrom!=0 && inviteFrom!=null) {
//    	    	UserModel userModelInvited =userService.findUserByInviteCode(inviteFrom);
//    	    	String UuidInvited=userModelInvited.getUuid();
//    	    	log.info("UuidInvited={}",UuidInvited);
//    	    	EnergyFriendsSharedResult energyFsResult = new EnergyFriendsSharedResult();
//    	    	String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
//    	    	powerService.saveFsEnergyBall(UuidInvited, now);
//    	    	energyFsResult=powerService.getFsEnergy();
//    	    	powerService.updateFsEnergyWallet(UuidInvited);
//    	    	
//    	    	//三级用户
//    	    	Integer inviteFromThreeLevel=userModelInvited.getInviteFrom();
//    	    	log.info("inviteFromThreeLevel={}",inviteFromThreeLevel);
//    	    		if(inviteFromThreeLevel!=0 && inviteFromThreeLevel!=null) {
//    	    			UserModel userModelThreeLevel=userService.findUserByInviteCode(inviteFromThreeLevel);
//    	    			String UuidThreeLevel=userModelThreeLevel.getUuid();
//    	    			log.info("UuidThreeLevel={}",UuidThreeLevel);
//    	    			powerService.saveFsEnergyBall(UuidThreeLevel, now);
//    	    	    	energyFsResult=powerService.getFsEnergy();
//    	    	    	powerService.updateFsEnergyWallet(UuidThreeLevel);
//    	    		}else {
//    	    			log.info("无三级用户！");
//    	    		}
//    	        return new ResponseEntity.Builder<Integer>()
//    	                .setData(1)
//    	                .setErrorCode(ErrorCode.SUCCESS)
//    	                .build();
//    	    	
//    	    }else {
//    	    	log.info("用户不是邀请用户，自主注册用户！");
//				return new ResponseEntity.Builder<Integer>()
//		                   .setData(1)
//		                   .setErrorCode(ErrorCode.SUCCESS)
//		                   .build();
//    	    }
//    }
//	}
	
	
	// inquireName
	@PostMapping(value="/inquireName")
	@ResponseBody
	public ResponseEntity<?> inquireName(@RequestBody UserModel userModel) {
		
		String name = userModel.getName();
		
	  if (userService.findUserByName(name) == null) {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0)
		        .setErrorCode(ErrorCode.SUCCESS).build();
	  } else {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0)
		        .setErrorCode(ErrorCode.GENERAL_ERROR).build();
		  }
	}
	
	
	
	@PostMapping(value="/inquireUserInfo")
	@ResponseBody
	public ResponseEntity<?> inquireUserInfo(){
		
	  Map<String, String> info = new HashMap<>();
	  UserModel userModel = ShiroUtils.getUser();
	  log.info("inviteCode11 {}", userModel.getInviteCode());	  
	  info.put("name", userModel.getName());
	  info.put("nickname", userModel.getNickname());
	  info.put("inviteCode", userModel.getInviteCode().toString());
	  info.put("gender", userModel.getGender());
	  info.put("address", userModel.getAddress());
	  info.put("birthday", userModel.getBirthday());
	  info.put("email", userModel.getEmail());
	  info.put("mobile", userModel.getMobile());
	  info.put("profile", userModel.getProfile());
	  log.info("****end****");
	  return new ResponseEntity.Builder<Map<String, String>>()
	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();	  
	}
	/**
	 * @author Ming Yang
	 * @param name
	 * @return UserModel 
	 */
	@PostMapping(value="/inquireTradeRecordUserInfo")
	@ResponseBody
	public ResponseEntity<?> inquireTradeRecordUserInfo(String name){
	  Map<String, String> info = new HashMap<>();
	  UserModel userModel = new UserModel();
	  userModel=userService.findUserByName(name);
	  log.info("inviteCode11 {}", userModel.getInviteCode());	  
	  info.put("name", userModel.getName());
	  info.put("nickname", userModel.getNickname());
	  info.put("inviteCode", userModel.getInviteCode().toString());
	  info.put("gender", userModel.getGender());
	  info.put("address", userModel.getAddress());
	  info.put("birthday", userModel.getBirthday());
	  info.put("email", userModel.getEmail());
	  info.put("mobile", userModel.getMobile());
	  log.info("****end****");
	  return new ResponseEntity.Builder<Map<String, String>>()
	      .setData(info)
	      .setErrorCode(ErrorCode.SUCCESS)
	      .build();	  
	}
	
	/*
	 * Name:upadateUserInfo
	 * Author:lvjisheng
	 * Date:2018.09.03
	 */
	@PostMapping(value="/updateUserInfo")
	@ResponseBody
	@WriteLog(value="UpdateUserInfo")
	public ResponseEntity<?> updateUserInfo(@RequestBody updateUserInfo userInfo){
	  Map<String,String> info = new HashMap<>();
	  UserModel userModel=new UserModel();   
      try {
    	 log.info("userName {}",userInfo.getName());
		 userModel.setName(ShiroUtils.getUser().getName());
		 userModel.setNickname(userInfo.getNickname());
	     userModel.setGender(userInfo.getGender());
	     userModel.setBirthday(userInfo.getBirthday());
	     userModel.setAddress(userInfo.getAddress());
    	 userService.updateUser(userModel); 
    	 UserModel userNewModel=new UserModel();
    	 userNewModel=userService.findUserByName(ShiroUtils.getUser().getName());
    	 ShiroUtils.setUser(userNewModel);
    	 //返回修改完成的数据
    	 info.put("name", userNewModel.getName());
    	 info.put("nickname", userNewModel.getNickname());
    	 info.put("gender", userNewModel.getGender());
    	 info.put("address", userNewModel.getAddress());
    	 info.put("birthday", userNewModel.getBirthday());
    	 info.put("mobile", userNewModel.getMobile());
    	 info.put("email", userNewModel.getEmail());
    	 info.put("inviteCode", userNewModel.getInviteCode().toString());
    	  log.info("修改成功");
    	  return new ResponseEntity.Builder<Map<String, String>>()
    		      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
      }catch(Exception e){
    	  log.info("修改失败"+e);
    	  return new ResponseEntity.Builder<Map<String, String>>()
    		      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
      }	     	  
	}
	
	/*
	 * Name:upLoadImg
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	@PostMapping(value="/upLoadImg")
	@WriteLog(value="UpLoadImg")
	//@RequiresRoles("admin")
	//@RequiresPermissions("userInfo:error")
	public ResponseEntity<?> upLoadImg(@RequestParam("file") MultipartFile file)
	{   
		log.info("doUpLoadImg-->start");
		File dir=new File(UPLOADED_FOLDER);
	  	 if(!dir.exists()){
	  	        dir.mkdirs();
	  	    }
	   	Map<String,String> info = new HashMap<>();
	   	if(file!=null)
	   	{	
	    //生成唯一的文件名
	    log.info("oriNmae="+file.getOriginalFilename());
	    String fileName = UUID.randomUUID().toString().replaceAll("-", "")+"-"+file.getOriginalFilename();
	    
	    try {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        Files.write(path, bytes);
        //String proPath=String.valueOf(path);
        String str="/image/profile/";        
        String proUrl=str+fileName;
        log.info("--------获取路径--------:{}",path);
        log.info("SYSTEM_USER_HOME={}",SYSTEM_USER_HOME);
	    log.info("fileName-->{}" + fileName);	    	    
	        //需要根据ID进行修改
	        UserModel userNewModel=new UserModel();
	        //获取用户名
	        String name=ShiroUtils.getUser().getName();
	        log.info("---userName-->{}" +name);
	        log.info("proUrl->{}",proUrl);
	        userNewModel.setName(name);
	        userNewModel.setProfile(proUrl);
	        userService.updateUserProfile(userNewModel);
	        //将头像的新地址存入缓存
	    	UserModel usermodel=ShiroUtils.getUser();
	    	usermodel.setProfile(proUrl);
	    	ShiroUtils.setUser(usermodel);
	        //图片存储的相对路径
	    	String fullLink = mediaServer.getImageHost() + proUrl;
	    	info.put("profile",fullLink);
	    	log.info("UpLoadSuccuss-->",fullLink);
	        return new ResponseEntity.Builder<Map<String, String>>()
		      	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	    } catch (Exception e){
	    	log.info("UpLoadFailed-->");
	    	 return new ResponseEntity.Builder<Map<String, String>>()
		      	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
	    }

	}
	    else {
	    	 return new ResponseEntity.Builder<Map<String, String>>()
		      	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
	    }
}
	   	
	
	/*
	 * Name:resetMobile
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	@RequestMapping(value = "/resetMobile", method = RequestMethod.POST)
	@WriteLog(value="ResetMobile")
	public ResponseEntity<?> resetMobile(@RequestBody UserModel userModel) throws Exception {
    Map<String,String> info=new HashMap<>();
	String name = ShiroUtils.getUser().getName();
	String mobile = userModel.getMobile();
	log.info("************ResetMobile start****************");

	log.info("userNickname =" + name);

	UserModel userNewModel = new UserModel();

	userNewModel.setMobile(mobile);

	userNewModel.setName(name);

	userService.resetMobileByName(userNewModel);
	
	UserModel usermodel=ShiroUtils.getUser();
	usermodel.setMobile(mobile);
	ShiroUtils.setUser(usermodel);

	log.info("userMobile ={}",userNewModel.getMobile());

	log.info("--------ResetMobile end-------");
    info.put("name",name);
    info.put("mobile",mobile);

	return new ResponseEntity.Builder<Map<String, String>>()
    	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	}
	
	/*
	 * Name:resetMail
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	@RequestMapping(value = "/resetMail", method = RequestMethod.POST)
	@WriteLog(value="ResetMail")
	public ResponseEntity<?> resetMail(@RequestBody UserModel userModel) throws Exception {

    Map<String,String> info=new HashMap<>();
	String name = ShiroUtils.getUser().getName();
	String email = userModel.getEmail();
	log.info("************ResetMail start****************");
	log.info("userMail-->"+email);
	UserModel userNewModel = new UserModel();

	userNewModel.setEmail(email);

	userNewModel.setName(name);

	userService.resetEmailByName(userNewModel);
	
	UserModel usermodel=ShiroUtils.getUser();
	usermodel.setEmail(email);
	ShiroUtils.setUser(usermodel);

	log.info("--------end-------");
	
    info.put("name",name);
    info.put("email",email);
	return new ResponseEntity.Builder<Map<String, String>>()
  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	}

	
    @RequestMapping(value="/checkPassword",method = RequestMethod.POST)
    @ResponseBody
    @WriteLog(value="CheckPassword")
    public ResponseEntity<?> checkPassword(@RequestBody UserModel userModel) {
       log.info("--------doCheckPassword start--------");
       Map<String,Boolean> info=new HashMap<>();
       String nameIn = userModel.getName(); 
       String passwordIn = userModel.getPassword();
       UserModel userNewModel = userService.findUserByName(nameIn); 
       log.info("name={}",nameIn);
       String saltDb = userNewModel.getSalt();  
       String userPasswordDb = userNewModel.getPassword(); 
       String userPasswordOu = new Md5Hash(nameIn+passwordIn+saltDb).toHex().toString(); 
       if (userPasswordOu.equals(userPasswordDb)){
        info.put("state", true);
        log.info("--------CheckPassword success--------");
		return new ResponseEntity.Builder<Map<String, Boolean>>()
			      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
		
       }else{
        info.put("state", false);
        log.info("--------CheckPassword failed--------");
		return new ResponseEntity.Builder<Map<String, Boolean>>()
			      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
       }
    }
	@PostMapping(value = "/sendMail")
	@ResponseBody
	@WriteLog(value="SendMail")
	public ResponseEntity<?> sendMail(@RequestBody UserModel userModel)throws ServletException, IOException {
 
		log.info("-----------sendMail start---------------");
		String email =userModel.getEmail();
		log.info(email);
		//String email=request.getParameter("email"); 
        //做一个判断邮箱和手机号
		Map<String, Boolean> info = new HashMap<>();

		 if(email.indexOf("com")!=-1)
         { String userEmail=email;
			try {	
				MailInfo mailInfo = new MailInfo();
                /**
                   * 通过yml来配置
                 */

				mailInfo.settoAddress(userEmail);
				//mailInfo.setfromAddress("3602745100@qq.com");
				mailInfo.setfromAddress(rootMail);
				mailInfo.setmailPassword(mailPassword);
				mailInfo.setmailUsername(mailName);
				mailInfo.setmailSubject(subject);
				
//				mailInfo.setfromAddress(mailBean.getRootMail());
//				mailInfo.setmailPassword(mailBean.getPassword());
//				mailInfo.setmailUsername(mailBean.getName());
//				mailInfo.setmailSubject(mailBean.getSubject());
	
				String mailcode = SendMailUtils.createRandomVcode();
				log.info("mailcode"+mailcode);
				
				Session session = ShiroUtils.getSession();
				//HttpSession session=request.getSession(true);
				// 把当前生成的验证码存在session中，当用户输入后进行对比
				session.setAttribute("mailCheckCode", mailcode);
	
				StringBuffer demo = new StringBuffer();
				demo.append("亲爱的：您好！<br><br>");
				demo.append("验证码：" + mailcode);
	
				mailInfo.setmailContent(demo.toString());
	
				System.out.println("获得页面数据");
	
				// 发送邮件是一件非常耗时的事情，因此这里开辟了另一个线程来专门发送邮件
				SendMailUtils send = new SendMailUtils(mailInfo);
				// 启动线程，线程启动之后就会执行run方法来发送邮件
				FutureTask<String> task = new FutureTask<String>(send); // 捕获任务执行的结果
				new Thread(task).start();
				task.get();
				info.put("state", true);
				return new ResponseEntity.Builder<Map<String, Boolean>>()
					      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
				
				
			} catch (Exception e) {
				e.printStackTrace();
				info.put("state", false);
				return new ResponseEntity.Builder<Map<String, Boolean>>()
					      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();

			}
			
         }else {
        	 
        	 info.put("state", false);
        	
        	 return new ResponseEntity.Builder<Map<String, Boolean>>()
				      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
        	 
         } 	 

	}
	
	// 对比前端输入验证码与邮箱中值是否一致
	@PostMapping(value = "/mailCheckCode")
	@ResponseBody
	@WriteLog(value="MailCheckCode")
	public ResponseEntity<?> mailCheckCode(@RequestBody AuthCode authCode) throws Exception {
		log.info("--------mailCheckCode start--------");

		String mailcode = authCode.getMailCode();
        log.info("code={}",mailcode);
		Map<String, Boolean> info = new HashMap<>();
        
		Session session=ShiroUtils.getSession();
		if (mailcode.equalsIgnoreCase((String) session.getAttribute("mailCheckCode"))) {
            info.put("state",true);
            log.info("--------mailCheckCode success--------");
			return new ResponseEntity.Builder<Map<String, Boolean>>()
				      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();

		} else {
			info.put("state",false);
			log.info("--------mailCheckCode failed-------");
			return new ResponseEntity.Builder<Map<String, Boolean>>()
				      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();

		}
	}
	
    @RequestMapping(value="/CheckUserEmail",method = RequestMethod.POST)
    @ResponseBody
    @WriteLog(value="CheckUserEmail")
    public ResponseEntity<?> CheckUserEmail(@RequestBody UserModel userModel) {
       log.info("*********start**********");
       String email= userModel.getEmail();
       log.info("email{}",email);
       List<String> userOEmail = userService.selectUserMail(email);

       Map<String,Boolean> info = new HashMap<>();
       
       if(userOEmail.size()>0) {
    	   info.put("state",false);
    	   log.info("*********end**********");
           return new ResponseEntity.Builder<Integer>()
      	          .setData(1)
      	          .setErrorCode(ErrorCode.GENERAL_ERROR)
      	          .build();
       }
       else {
    	   info.put("state",true);
    	   log.info("*********end**********");
           return new ResponseEntity.Builder<Integer>()
     	          .setData(0)
     	          .setErrorCode(ErrorCode.SUCCESS)
     	          .build();
       }    
}
    @RequestMapping(value="/CheckUserMobile",method = RequestMethod.POST)
    @ResponseBody
    @WriteLog(value="CheckUserMobile")
    public ResponseEntity<?> CheckUserMobile(@RequestBody UserModel userModel) {
       log.info("*********CheckUserMobile start**********");
       String mobile= userModel.getMobile();
       log.info("mobile{}",mobile);
       List<String> userOMobile = userService.selectUserMobile(mobile);
       Map<String,Boolean> info = new HashMap<>();
       
       if(userOMobile.size()>0) {
    	   info.put("state",false);
    	   log.info("*********endfailed**********");
           return new ResponseEntity.Builder<Integer>()
      	          .setData(1)
      	          .setErrorCode(ErrorCode.GENERAL_ERROR)
      	          .build();
       }
       else {
    	   info.put("state",true);
    	   log.info("*********endsuccess**********");
           return new ResponseEntity.Builder<Integer>()
     	          .setData(0)
     	          .setErrorCode(ErrorCode.SUCCESS)
     	          .build();
       }
      
    }
    
    /**
     * 根据手机号查询用户名
     */
	@PostMapping(value = "/inquireUserInfoByMobile")
    @ResponseBody
	public ResponseEntity<?> inquireUserInfoByMobile(@RequestBody UserModel userModel){
		String mobile=userModel.getMobile();
		Map<String,String> info =new HashMap<String,String>();
        if(userService.findUserByMobile(mobile)!=null){
        	 String name=userService.findUserByMobile(mobile);
        	 info.put("name", name);
        	 return new ResponseEntity.Builder<Map<String,String>>()
   	              .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
        }else{
        	log.info("用户名不存在");
        	return new ResponseEntity.Builder<String>()
     	          .setData("empty").setErrorCode(ErrorCode.GENERAL_ERROR).build();
        }	   
     }
    /**
     * 重置密码
     */
	@PostMapping(value = "/resetPassword")
    @ResponseBody
	public ResponseEntity<?> resetPassword(@RequestBody UserModel userModel){
 	       
      String name=userModel.getName();
  	  UserModel userNewModel=new UserModel();
  	  userNewModel=userService.findUserByName(name);
	  String password=userModel.getPassword();
	  String salt=userNewModel.getSalt();
	  String calcPassword=new Md5Hash(name+password+salt).toHex().toString();
	  log.info(calcPassword);
	  userNewModel.setPassword(calcPassword);
		  try{
			  userService.updateUserPassworde(userNewModel);
			  return new ResponseEntity.Builder<Integer>()
		     	          .setData(0).setErrorCode(ErrorCode.SUCCESS).build();	 
		  }catch(Exception e){
			  log.info(e.getMessage());
			  return new ResponseEntity.Builder<Integer>()
		 	          .setData(1).setErrorCode(ErrorCode.GENERAL_ERROR).build();
		  }         
     }
	
	@RequestMapping(value = "/sendMobile", method = RequestMethod.POST)
    public ResponseEntity<?> sendMobile(@RequestBody MobileModel mobileModel){
		Map<String,Boolean> info=new HashMap<>();
		log.info("****start****");
		//后续"+86"可以在前端进行修改
		String mobile = "+86"+mobileModel.getMobile();
		String SignName="OASESCHAIN";
		String content="";
        //获取验证码
		vcode=MessageService.createRandomVcode();
		if(mobileModel.getContent()!=null){
			content = mobileModel.getContent();
		}else {
		content = "【"+SignName+"】"+"验证码为"+vcode+",您正在尝试变更重要信息，请妥善保管账户信息。";
		}
        PublishResult publishResult = messageService.sendSMSMessage(mobile,content);
        log.info(publishResult.toString());
		info.put("state",true);
		log.info("code={}",vcode);
        log.info("****end****");
        return new ResponseEntity.Builder<Map<String,Boolean>>()
		  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();		
    }
	/*
	 * Name:sendMobile--Aliyun
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
//	@RequestMapping(value = "/sendMobile", method = RequestMethod.POST)
//	@WriteLog(value="SendMobile")
////	public ResponseEntity<?> sendMobile(@RequestBody UserModel userModel) throws Exception {
//	public ResponseEntity<?> sendMobile(HttpServletRequest request) throws Exception {
//    Map<String,Boolean> info=new HashMap<>();
//	
//    log.info("-----------sendMobile start---------------");
//	
////	String mobile = userModel.getMobile();
//    
//    //String mobile=request.getParameter("mobile");
//    String oldMobile =request.getReader().readLine();
//    int length=11+2;
//    String mobile = oldMobile.substring(oldMobile.indexOf(":")+2,oldMobile.indexOf(":")+length);
//    log.info(mobile);
//	try {	
//		vcode = AuthenticationUtils.createRandomVcode();
//		log.info("vcode = "+vcode);
////		Session session = ShiroUtils.getSession();
//		HttpSession session=request.getSession();
//		
//		session.setAttribute("mobileCheckCode", vcode);
//		log.info("sessionCheckCode{}",session.getAttribute("mobileCheckCode"));
//		AuthenticationUtils sms = new AuthenticationUtils();		
//		if(sms.SendCode(mobile,vcode).getCode().equals("OK")){
//				info.put("state",true);
//				log.info("success");
//				return new ResponseEntity.Builder<Map<String, Boolean>>()
//				  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();			
//		}else{
//			
//			info.put("state",false);
//			log.info("failure1");
//			return new ResponseEntity.Builder<Map<String, Boolean>>()
//			  	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
//		}
//		
//	} catch (Exception e) {
//		log.info(e.getMessage());
//		log.info("failure2");
//		e.printStackTrace();	
//		info.put("state",false);
//		return new ResponseEntity.Builder<Map<String, Boolean>>()
//		  	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
//	}		
//}
	
	@RequestMapping(value = "/mobileCheckCode", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> mobileCheckCode(@RequestBody AuthCode authCode) throws Exception {
		log.info("--------mobileCheckCode   start--------");
		String mobilecode=authCode.getMobileCode();
	    log.info(mobilecode);
		Map<String,Boolean> info = new HashMap<>();
		try{
			if (mobilecode.equalsIgnoreCase(vcode)) {
				log.info("success");
				info.put("state",true);	
				return new ResponseEntity.Builder<Map<String, Boolean>>()
				  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();		
			} else {
				info.put("state",false);
				log.info("failure");
				return new ResponseEntity.Builder<Map<String, Boolean>>()
				  	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();	
			}
		}catch(Exception e){
			log.info(e.getMessage());
			e.getStackTrace();		
			info.put("state",false);
			return new ResponseEntity.Builder<Map<String, Boolean>>()
			  	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
		}		
	}
	
	@PostMapping(value="/inqureAllUserIdentityInfo")
	@ResponseBody
	public ResponseEntity<?> selectAllUserIdentityInfo(){
		List<UserIdentityCardModel> userIdentityCardModelList=userService.selectAllUserIdentityCard();
		return new ResponseEntity.Builder<List<UserIdentityCardModel>>()
		  	      .setData(userIdentityCardModelList)
		  	      .setErrorCode(ErrorCode.SUCCESS)
		  	      .build();
			}
	
	@PostMapping(value="/inqureUserIdentityInfo")
	@ResponseBody
	public ResponseEntity<?> inqureUserIdentityInfo(){
		String userName=ShiroUtils.getLoginName();
		List<UserIdentityCardModel> userIdentityCardModelList=userService.selectUserIdentityByUserName(userName);
		UserIdentityCardModel userIdentityCardModel=userIdentityCardModelList.get(0);
		log.info("userIdentityCardModel={}",userIdentityCardModel.getCreated());
		if(userIdentityCardModel.getUpdated()==null)
		{
			String updated=userIdentityCardModel.getCreated();
			userIdentityCardModel.setUpdated(updated);
		}
		if(userIdentityCardModel.getRemark()==null)
		{
				String remark="empty";
				userIdentityCardModel.setRemark(remark);
		}
		if(userIdentityCardModel.getUserIdentityName()==null) 
		{
			String userIdentityName="empty";
			userIdentityCardModel.setUserIdentityName(userIdentityName);
		}
		if(userIdentityCardModel.getUserIdentityNumber()==null)
		{
			String userIdentityNumber="empty";
			userIdentityCardModel.setUserIdentityNumber(userIdentityNumber);
		}
		if(userIdentityCardModel.getFrontOfPhoto()==null)
		{
			String frontOfPhoto="empty";
			userIdentityCardModel.setFrontOfPhoto(frontOfPhoto);
		}
		if(userIdentityCardModel.getBackOfPhoto()==null)
		{
			String backOfPhoto="empty";
			userIdentityCardModel.setBackOfPhoto(backOfPhoto);
		}
		if(userIdentityCardModel.getHoldInHand()==null)
		{
			String holdInHand="empty";
			userIdentityCardModel.setHoldInHand(holdInHand);
		}
			return new ResponseEntity.Builder<UserIdentityCardModel>()
			  	      .setData(userIdentityCardModel)
			  	      .setErrorCode(ErrorCode.SUCCESS)
			  	      .build();
			}
	@PostMapping(value="/upLoadUserIdentityInfo")
	@ResponseBody
	public ResponseEntity<?> upLoadUserIdentityInfo(@RequestParam("file") MultipartFile file){
		
		String userName=ShiroUtils.getLoginName();
		UserIdentityCardModel userIdentityCardModel=userIdentityCardModelMapper.selectUserIdentityByUserNameVerifyStatus(userName);
		
		Map<String,String> info = new HashMap<>();
		File dir=new File(IDENTITY_UPLOADED);
	  	 if(!dir.exists()){
	  	   dir.mkdirs();
	  	  }
	  	String str="/image/identityCard/";

	  	//日期时间生成唯一标识文件名
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
			String uniqueFileName = format.format(new Date())+new Random().nextInt()+"-"+file.getOriginalFilename();
			String fileName=file.getOriginalFilename();
			fileName=fileName.substring(0, 4);
			log.info("fileName={}",fileName);
			try {
				byte[] bytes = file.getBytes();
	            Path path = Paths.get(IDENTITY_UPLOADED + uniqueFileName);
	            Files.write(path, bytes);	  
			}catch (Exception e)
			{
	    		log.info("身份证上传失败"+e);
			}
			if(fileName.equals("face") && userIdentityCardModel != null )
			{
				Integer verifyStatus=0;
				String newfrontOfPhoto=str+uniqueFileName;
				String created=DateUtils.getTime();
				String updated=created;
				
				userIdentityCardModel.setUserName(userName);
				userIdentityCardModel.setFrontOfPhoto(newfrontOfPhoto);
				userIdentityCardModel.setVerifyStatus(verifyStatus);
				userIdentityCardModel.setCreated(created);
				userIdentityCardModel.setUpdated(updated);
				
				userIdentityCardModelMapper.updateUserIdentityCardByFrontOfPhoto(userIdentityCardModel);
				
				UserIdentityCardModel userNewIdentityCardModel=userIdentityCardModelMapper.selectUserIdentityByUserNameVerifyStatus(userName);
				String newFrontOfPhoto=userNewIdentityCardModel.getFrontOfPhoto();
				String frontOfPhoto = mediaServer.getImageHost() + newFrontOfPhoto;
				info.put("frontOfPhoto", frontOfPhoto);
				return new ResponseEntity.Builder<Map<String,String>>()
				  	      .setData(info)
				  	      .setErrorCode(ErrorCode.SUCCESS)
				  	      .build();
			}else if(fileName.equals("face") && userIdentityCardModel == null )
			{
				UserIdentityCardModel newUserIdentityCardModel=new UserIdentityCardModel();
				Integer verifyStatus=0;
				String created=DateUtils.getTime();
				String updated=created;
				String newfrontOfPhoto=str+uniqueFileName;
				
				newUserIdentityCardModel.setUserName(userName);
				newUserIdentityCardModel.setFrontOfPhoto(newfrontOfPhoto);
				newUserIdentityCardModel.setVerifyStatus(verifyStatus);
				newUserIdentityCardModel.setCreated(created);
				newUserIdentityCardModel.setUpdated(updated);
				
				userIdentityCardModelMapper.insertUserIdentityCard(newUserIdentityCardModel);
				
				UserIdentityCardModel userNewIdentityCardModel=userIdentityCardModelMapper.selectUserIdentityByUserNameVerifyStatus(userName);
				String newFrontOfPhoto=userNewIdentityCardModel.getFrontOfPhoto();
				String frontOfPhoto = mediaServer.getImageHost() + newFrontOfPhoto;
				info.put("frontOfPhoto", frontOfPhoto);
				return new ResponseEntity.Builder<Map<String,String>>()
				  	      .setData(info)
				  	      .setErrorCode(ErrorCode.SUCCESS)
				  	      .build();
			   }else if(fileName.equals("back") && userIdentityCardModel != null) 
				{
				    Integer verifyStatus=0;
					String created=DateUtils.getTime();
					String updated=created;
					String newbackOfPhoto=str+uniqueFileName;
					
					userIdentityCardModel.setUserName(userName);
					userIdentityCardModel.setBackOfPhoto(newbackOfPhoto);
					userIdentityCardModel.setVerifyStatus(verifyStatus);
					userIdentityCardModel.setCreated(created);
					userIdentityCardModel.setUpdated(updated);
					
					userIdentityCardModelMapper.updateUserIdentityCardByBackOfPhoto(userIdentityCardModel);
				
					UserIdentityCardModel userNewIdentityCardModel=userIdentityCardModelMapper.selectUserIdentityByUserNameVerifyStatus(userName);
					String newBacktOfPhoto=userNewIdentityCardModel.getBackOfPhoto();
					String backOfPhoto = mediaServer.getImageHost() + newBacktOfPhoto;
					info.put("backOfPhoto", backOfPhoto);
					return new ResponseEntity.Builder<Map<String,String>>()
					  	      .setData(info)
					  	      .setErrorCode(ErrorCode.SUCCESS)
					  	      .build();
				}else if(fileName.equals("back") && userIdentityCardModel == null)
				{
					UserIdentityCardModel newUserIdentityCardModel=new UserIdentityCardModel();
					Integer verifyStatus=0;
					String created=DateUtils.getTime();
					String updated=created;
					String newbackOfPhoto=str+uniqueFileName;
					
					newUserIdentityCardModel.setUserName(userName);
					newUserIdentityCardModel.setBackOfPhoto(newbackOfPhoto);
					newUserIdentityCardModel.setVerifyStatus(verifyStatus);
					newUserIdentityCardModel.setCreated(created);
					newUserIdentityCardModel.setUpdated(updated);
					
					userIdentityCardModelMapper.insertUserIdentityCard(newUserIdentityCardModel);
					
					UserIdentityCardModel userNewIdentityCardModel=userIdentityCardModelMapper.selectUserIdentityByUserNameVerifyStatus(userName);
					String newBacktOfPhoto=userNewIdentityCardModel.getBackOfPhoto();
					String backOfPhoto = mediaServer.getImageHost() + newBacktOfPhoto;
					info.put("backOfPhoto", backOfPhoto);
					return new ResponseEntity.Builder<Map<String,String>>()
					  	      .setData(info)
					  	      .setErrorCode(ErrorCode.SUCCESS)
					  	      .build();
				}else if(fileName.equals("hand") && userIdentityCardModel != null) 
				{
					Integer verifyStatus=0;
					String newholdInHand=str+uniqueFileName;
					String created=DateUtils.getTime();
					String updated=created;
	
					userIdentityCardModel.setUserName(userName);
					userIdentityCardModel.setHoldInHand(newholdInHand);
					userIdentityCardModel.setVerifyStatus(verifyStatus);
					userIdentityCardModel.setCreated(created);
					userIdentityCardModel.setUpdated(updated);
					
					userIdentityCardModelMapper.updateUserIdentityCardByHoldInHand(userIdentityCardModel);
					
					UserIdentityCardModel userNewIdentityCardModel=userIdentityCardModelMapper.selectUserIdentityByUserNameVerifyStatus(userName);
					String newHoldInHand=userNewIdentityCardModel.getHoldInHand();
					String holdInHand = mediaServer.getImageHost() + newHoldInHand;
					info.put("holdInHand", holdInHand);
					return new ResponseEntity.Builder<Map<String,String>>()
					  	      .setData(info)
					  	      .setErrorCode(ErrorCode.SUCCESS)
					  	      .build();
			  }else if(fileName.equals("hand") && userIdentityCardModel == null)
			  {
				    UserIdentityCardModel newUserIdentityCardModel=new UserIdentityCardModel();
				    Integer verifyStatus=0;
					String created=DateUtils.getTime();
					String updated=created;
					String newholdInHand=str+uniqueFileName;
					
					newUserIdentityCardModel.setUserName(userName);
					newUserIdentityCardModel.setHoldInHand(newholdInHand);
					newUserIdentityCardModel.setVerifyStatus(verifyStatus);
					newUserIdentityCardModel.setCreated(created);
					newUserIdentityCardModel.setUpdated(updated);
					
					userIdentityCardModelMapper.insertUserIdentityCard(newUserIdentityCardModel);
					
					UserIdentityCardModel userNewIdentityCardModel=userIdentityCardModelMapper.selectUserIdentityByUserNameVerifyStatus(userName);
					String newHoldInHand=userNewIdentityCardModel.getHoldInHand();
					String holdInHand = mediaServer.getImageHost() + newHoldInHand;
					info.put("holdInHand", holdInHand);
					return new ResponseEntity.Builder<Map<String,String>>()
					  	      .setData(info)
					  	      .setErrorCode(ErrorCode.SUCCESS)
					  	      .build();
			   }else{
				   log.info("文件上传失败，请重试！");
				   return new ResponseEntity.Builder<Integer>()
					  	      .setData(1)
					  	      .setErrorCode(ErrorCode.GENERAL_ERROR)
					  	      .build();
			   }
	}
	
	/**
	 * @author Ming Yang
	 * @return 提交状态
	 */
	@PostMapping(value="/confirmSubmitUserIdentifyInfo")
    @ResponseBody
    @WriteLog(value="confirmSubmitUserIdentifyInfo")
    public ResponseEntity<?> confirmSubmitUserIdentifyInfo(@RequestBody UserIdentityCardModel userIdentityCardModelInfo){
		String userName=ShiroUtils.getLoginName();
		UserIdentityCardModel userNewIdentityCardModel=userIdentityCardModelMapper.selectUserIdentityByUserNameVerifyStatus(userName);
		
		String frontOfPhoto=userIdentityCardModelInfo.getFrontOfPhoto();
		String backOfPhoto=userIdentityCardModelInfo.getBackOfPhoto();
		String holdInHand=userIdentityCardModelInfo.getHoldInHand();
		
		if(frontOfPhoto !=null && backOfPhoto !=null && holdInHand !=null) {
			Integer verifyStatus=1;
			String created=DateUtils.getTime();
			String updated=created;
			userNewIdentityCardModel.setFrontOfPhoto(frontOfPhoto);
			userNewIdentityCardModel.setBackOfPhoto(backOfPhoto);
			userNewIdentityCardModel.setHoldInHand(holdInHand);
			userNewIdentityCardModel.setVerifyStatus(verifyStatus);
			userNewIdentityCardModel.setCreated(created);
			userNewIdentityCardModel.setUpdated(updated);
			
			
			userIdentityCardModelMapper.updateUserIdentityCardByVerifyStatus(userNewIdentityCardModel);
			
			return new ResponseEntity.Builder<Integer>()
			  	      .setData(0)
			  	      .setErrorCode(ErrorCode.SUCCESS)
			  	      .build();
			}else {
			
			log.info("不是三张照片");
			return new ResponseEntity.Builder<Integer>()
			  	      .setData(1)
			  	      .setErrorCode(ErrorCode.GENERAL_ERROR)
			  	      .build();
		}
	}
	
	@PostMapping(value="/checkIdentityNumber")
    @ResponseBody
    @WriteLog(value="checkIdentityNumber")
    public ResponseEntity<?> checkIdentityNumber(@RequestBody UserIdentityCardModel userIdentityCardModel) {
       log.info("*********checkIdentityNumber start**********");
       String userIdentityNumber=userIdentityCardModel.getUserIdentityNumber();
       userIdentityCardModel=userIdentityCardModelMapper.selectUserByIdentityNumber(userIdentityNumber);
       if(userIdentityCardModel!=null)
       {
    	   log.info("***该身份证号码存在***");
    	   return new ResponseEntity.Builder<Integer>()
			  	      .setData(0)
			  	      .setErrorCode(ErrorCode.GENERAL_ERROR)
			  	      .build();
       }else
       {
    	   log.info("***该身份证号码不存在***");
    	   return new ResponseEntity.Builder<Integer>()
			  	      .setData(1)
			  	      .setErrorCode(ErrorCode.SUCCESS)
			  	      .build();
       }
    }
	
	@PostMapping(value="/checkUserIdentity")
    @ResponseBody
    @WriteLog(value="checkUserIdentity")
    public ResponseEntity<?> checkUserIdentity(@RequestBody UserIdentityCardModel userIdentityCardModelInfo) {
		UserIdentityCardModel userIdentityCardModel = new UserIdentityCardModel();
		
		userIdentityCardModel.setUuid(userIdentityCardModelInfo.getUuid());
		userIdentityCardModel.setUserIdentityName(userIdentityCardModelInfo.getUserIdentityName());
		userIdentityCardModel.setUserIdentityNumber(userIdentityCardModelInfo.getUserIdentityNumber());
		userIdentityCardModel.setVerifyStatus(userIdentityCardModelInfo.getVerifyStatus());
		userIdentityCardModel.setRemark(userIdentityCardModelInfo.getRemark());
		
		userIdentityCardModelMapper.updateUserIdentityCardByNameNumberRemarkVerifyStatus(userIdentityCardModel);
		
		return new ResponseEntity.Builder<UserIdentityCardModel>()
		  	      .setData(userIdentityCardModel)
		  	      .setErrorCode(ErrorCode.SUCCESS)
		  	      .build();
	}

	/**
	 * 增加system用户,admin用户及三个钱包账号
	 */
	@PostConstruct
	private void registerSystemAndAdmin() {
		createUserAfterStart("ADMIN","Dapp000OAS");
		createUserAfterStart("SYSTEM",generatePassword(30));
		createUserAfterStart("FIRSTONE","Dapp880OAS");
		log.info("system交易钱包地址为"+ethWalletService.getSystemAddress());
	}
	
	
	
	
	/**
	 * @author lvjisheng
	 * @param IMEI,name
	 * @return 
	 * @throws 如果IMEI为空,则视为重置
	 */
	@PostMapping(value="/updateIMEI")
    @ResponseBody
    @WriteLog(value="updateIMEI")
    public ResponseEntity<?> updateIMEI(@RequestBody IMEIModel imeiModel) {
		Map<String,Object> info=new HashMap<>(); 
		UserModel userNeWModel=new UserModel();
		String IMEINew=imeiModel.getIMEI();
		String IMEIOri=userModelMapper.selectByName(imeiModel.getName()).getIMEI();
		userNeWModel.setIMEI(IMEINew);
		userNeWModel.setName(imeiModel.getName());
		info.put("state",true);	
		userModelMapper.updateIMEI(userNeWModel);		
		String uuid=userModelMapper.selectByName(imeiModel.getName()).getUuid();
		//删除账号和手机的绑定
		UserFacility userFacility=new UserFacility();
		userFacility.setUuid(uuid);
		userFacility.setIMEI(IMEIOri);
		if(userFacilityMapper.inquireUserFacilityByModel(userFacility)!=null)
		       userFacilityMapper.deleteUserFacility(uuid);		
		//删除旧角色,添加新角色
//		String uuid =userService.findUserByName(imeiModel.getName()).getUuid();
//		roleService.updateUerRoles(uuid,imeiModel.getRoleId());
		
		return new ResponseEntity.Builder<Map<String,Object>>()
		  	      .setData(info)
		  	      .setErrorCode(ErrorCode.SUCCESS)
		  	      .build();
	}		
	
	/**
	 * @author lvjisheng
	 * @param offset,limit,roleId
	 * @category 根据角色名称查找相应的用户
	 * @return
	 */
	@PostMapping(value="/selectAllUsers")
    @ResponseBody
    @WriteLog(value="selectAllUsers")
    public ResponseEntity<?> selectAllUsers(@RequestBody PageDomain<Integer> pageInfo){
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
		    
	  	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	  	    
	  	    
	        List<UserDetailModel> userList=userService.selectUsersByPage(offset, limit,pageInfo.getRoleId(),searchValue);
	        
	        Integer count = userService.countUsers(pageInfo.getRoleId());			        			     
	        
	        PageDomain<UserDetailModel> pageUserDetail = new PageDomain<>();
	        pageUserDetail.setTotal(count);
	        pageUserDetail.setAsc("asc");
	        pageUserDetail.setOffset(offset);
	        pageUserDetail.setPageNum(pageNum);
	        pageUserDetail.setPageSize(pageSize);
	        pageUserDetail.setRows(userList);

	        return new ResponseEntity.Builder<PageDomain<UserDetailModel>>()
	                .setData(pageUserDetail)
	                .setErrorCode(ErrorCode.SUCCESS)
	                .build();
	}
	
	/**
	 * @author lvjisheng
	 * @param name,roleId
	 * @return 
	 * @throws 如果IMEI为空,则视为重置
	 */
	@PostMapping(value="/updateUserRole")
    @ResponseBody
    @WriteLog(value="updateUserRole")
    public ResponseEntity<?> updateUserRole(@RequestBody UserRole userRole) {
		Map<String,Object> info=new HashMap<>(); 	
		info.put("state",true);		
		//删除旧角色,添加新角色
		String uuid =userService.findUserByName(userRole.getName()).getUuid();
		roleService.updateUerRoles(uuid,userRole.getRoleId());		
		return new ResponseEntity.Builder<Map<String,Object>>()
		  	      .setData(info)
		  	      .setErrorCode(ErrorCode.SUCCESS)
		  	      .build();
	}
	
	/**
	 * @author lvjisheng
	 * @param name,status
	 * @return 
	 * @throws 如果IMEI为空,则视为重置
	 */
	@PostMapping(value="/updateUserStatus")
    @ResponseBody
    @WriteLog(value="updateUserStatus")
    public ResponseEntity<?> updateUserStatus(@RequestBody UserStatus userStatus) {
		Map<String,Object> info=new HashMap<>(); 	
		info.put("state",true);	
		userModelMapper.updateUserStatus(userStatus.getStatus(), userStatus.getName());
		return new ResponseEntity.Builder<Map<String,Object>>()
		  	      .setData(info)
		  	      .setErrorCode(ErrorCode.SUCCESS)
		  	      .build();

	}
	

	/**
	 * @author lvjisheng
	 * @param offset,limit,roleId
	 * @category 根据角色名称查找相应的用户
	 * @return
	 */
	@PostMapping(value="/inquireUserKYCInfo")
    @ResponseBody
    @WriteLog(value="inquireUserKYCInfo")
    public ResponseEntity<?> inquireUserKYCInfo(@RequestBody UserIdentityCardModel kycModel){
		    UserDetailModel newKYCModel =userIdentityCardModelMapper.inquireUserKYCInfo(kycModel.getUserName());
		    ErrorCode errorCode=ErrorCode.SUCCESS;
		    if(newKYCModel==null)
		    errorCode=ErrorCode.GENERAL_ERROR;
	        return new ResponseEntity.Builder<UserDetailModel>()
	                .setData(newKYCModel)
	                .setErrorCode(errorCode)
	                .build();

	}
	/**
	 * 生成随即字串
	 * @param length
	 * @return
	 */
	private String generatePassword(int length) {
		String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	}
	
	private void createUserAfterStart(String name,String password) {
		UserModel userModel = new UserModel();
		userModel.setName(name);
		userModel.setStatus(1);
		userModel.setPassword(password);
		
		String uuid = UuidUtils.getPrefixUUID(UuidPrefix.USER_MODEL);
		ErrorCode code = userService.addUser(uuid, userModel);
		if(code.getCode() == 0) { // 用户不存在
			ethWalletService.create(uuid, password);
			if(name.equals("SYSTEM")) {
				BigDecimal value = new BigDecimal("10000000");
				userWalletService.createAccountByMoney(uuid,value);
				 //system手续费记录
				userWalletService.insertSystemInit(value);
			  
			}else if(name.equals("FIRSTONE")) {
				BigDecimal value = new BigDecimal("8800000");
				userWalletService.createAccountByMoney(uuid,value);
				userWalletService.insertFirstOneInit(value);
			}
			else {
				userWalletService.createAccountByMoney(uuid,BigDecimal.ZERO);
			}
			energyPointService.create(uuid);
			
			//给用户赋予默认角色1
			UserRole userRole=new UserRole();
		    userRole.setUuid(uuid);
			if(name.equals("FIRSTONE")) {
				userRole.setRoleId(2);
			}else {
				userRole.setRoleId(1);
			}
		    String now =DateUtils.getTime();
		    userRole.setRolePriority(1);
		    userRole.setCreated(now);
		    userRoleModelMapper.insertUserRole(userRole);
		    
		    log.info(name+"用户创建成功");
		}else {
			log.info(name+"用户已存在");
		}
		
	}
}
