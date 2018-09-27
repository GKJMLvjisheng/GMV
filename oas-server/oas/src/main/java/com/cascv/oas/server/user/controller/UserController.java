package com.cascv.oas.server.user.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.FutureTask;
import javax.servlet.ServletException;
import org.apache.commons.lang.SystemUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.service.EnergyWalletService;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.service.PowerService;
import com.cascv.oas.server.energy.vo.EnergyFriendsSharedResult;
import com.cascv.oas.server.log.annotation.WriteLog;
import com.cascv.oas.server.news.config.MediaServer;
import com.cascv.oas.server.user.model.MailInfo;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.user.wrapper.AuthCode;
import com.cascv.oas.server.user.wrapper.LoginResult;
import com.cascv.oas.server.user.wrapper.LoginVo;
import com.cascv.oas.server.user.wrapper.RegisterConfirm;
import com.cascv.oas.server.user.wrapper.RegisterResult;
import com.cascv.oas.server.utils.AuthenticationUtils;
import com.cascv.oas.server.utils.SendMailUtils;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.user.wrapper.updateUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")

public class UserController {
	
  @Autowired
  private UserService userService;
  @Autowired
  private MediaServer mediaServer;
  @Autowired
  private PowerService powerService;
  @Autowired
	private EthWalletService ethWalletService;
	
	@Autowired
	private UserWalletService userWalletService;
	
	@Autowired
	private EnergyWalletService energyPointService;
  
	@ApiOperation(value="Login", notes="")
	@PostMapping(value="/login")
	@ResponseBody
	@WriteLog(value="Login")
	public ResponseEntity<?> userLogin(@RequestBody LoginVo loginVo) {
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
          loginResult.fromUserModel(ShiroUtils.getUser());
          return new ResponseEntity.Builder<LoginResult>()
              .setData(loginResult).setErrorCode(ErrorCode.SUCCESS)
              .build();
      } catch (AuthenticationException e)  {
          return new ResponseEntity.Builder<LoginResult>()
                .setData(loginResult)
                .setErrorCode(ErrorCode.GENERAL_ERROR).build();
      }
	}
	
	// Register
	@ApiOperation(value="Register", notes="")
	@PostMapping(value="/register")
	@ResponseBody
	@Transactional
	@WriteLog(value="Register")
	public ResponseEntity<?> register(@RequestBody UserModel userModel) {

	    String password = userModel.getPassword();
		RegisterResult registerResult = new RegisterResult();
		String uuid = UuidUtils.getPrefixUUID(UuidPrefix.USER_MODEL);
		EthWallet ethHdWallet = ethWalletService.create(uuid, password);
		userWalletService.create(uuid);
		energyPointService.create(uuid);
		ErrorCode ret = userService.addUser(uuid, userModel);
		log.info("inviteCode {}", userModel.getInviteCode());
  	if (ret.getCode() == ErrorCode.SUCCESS.getCode()) {
  	  registerResult.setMnemonicList(EthWallet.fromMnemonicList(ethHdWallet.getMnemonicList()));
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
	  
	  //根据前端返回uuid找到新注册用户
	  UserModel userModel = userService.findUserByUuid(registerConfirm.getUuid());
	  
    if (userModel != null && registerConfirm.getCode() != null && registerConfirm.getCode() != 0) {
    		//用户注册失败
			String uuid = userModel.getUuid();
			System.out.println(uuid);
			ethWalletService.destroy(uuid);
			userWalletService.destroy(uuid);
			energyPointService.destroy(uuid);
            userService.deleteUserByUuid(uuid);
            //注册未完成
			return new ResponseEntity.Builder<Integer>()
	                   .setData(2)
	                   .setErrorCode(ErrorCode.GENERAL_ERROR)
	                   .build();
    }else {
    		//用户注册成功
    	    Integer inviteFrom=userModel.getInviteFrom();
    	    log.info("inviteFrom={}",inviteFrom);
    	    if(inviteFrom!=0 && inviteFrom!=null) {
    	    	UserModel userModelInvited =userService.findUserByInviteCode(inviteFrom);
    	    	String UuidInvited=userModelInvited.getUuid();
    	    	log.info("UuidInvited={}",UuidInvited);
    	    	EnergyFriendsSharedResult energyFsResult = new EnergyFriendsSharedResult();
    	    	String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
    	    	powerService.saveFsEnergyBall(UuidInvited, now);
    	    	energyFsResult=powerService.getFsEnergy();
    	    	powerService.updateFsEnergyWallet(UuidInvited);
    	    	
    	    	//三级用户
    	    	Integer inviteFromThreeLevel=userModelInvited.getInviteFrom();
    	    	log.info("inviteFromThreeLevel={}",inviteFromThreeLevel);
    	    		if(inviteFromThreeLevel!=0 && inviteFromThreeLevel!=null) {
    	    			UserModel userModelThreeLevel=userService.findUserByInviteCode(inviteFromThreeLevel);
    	    			String UuidThreeLevel=userModelThreeLevel.getUuid();
    	    			log.info("UuidThreeLevel={}",UuidThreeLevel);
    	    			powerService.saveFsEnergyBall(UuidThreeLevel, now);
    	    	    	energyFsResult=powerService.getFsEnergy();
    	    	    	powerService.updateFsEnergyWallet(UuidThreeLevel);
    	    		}else {
    	    			log.info("无三级用户！");
    	    		}
    	        return new ResponseEntity.Builder<Integer>()
    	                .setData(1)
    	                .setErrorCode(ErrorCode.SUCCESS)
    	                .build();
    	    	
    	    }else {
    	    	log.info("用户不是邀请用户，自主注册用户！");
				return new ResponseEntity.Builder<Integer>()
		                   .setData(1)
		                   .setErrorCode(ErrorCode.SUCCESS)
		                   .build();
    	    }
    }
	}
	
	
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
	public ResponseEntity<?> upLoadImg(@RequestParam("file") MultipartFile file)
	{   
		 String SYSTEM_USER_HOME=SystemUtils.USER_HOME;
		 String UPLOADED_FOLDER =SYSTEM_USER_HOME+File.separator+"Temp"+File.separator+"Image" + File.separator+"profile"+File.separator;	
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
	    	log.info("UpLoadSuccuss-->");
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
	/*
	 * Name:sendMobile
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	@RequestMapping(value = "/sendMobile", method = RequestMethod.POST)
	@WriteLog(value="SendMobile")
	public ResponseEntity<?> sendMobile(@RequestBody UserModel userModel) throws Exception {
    Map<String,Boolean> info=new HashMap<>();
	
    log.info("-----------sendMobile start---------------");
	
	String mobile = userModel.getMobile();

	try {
	
		String vcode = AuthenticationUtils.createRandomVcode();
		log.info("vcode = "+vcode);
		Session session = ShiroUtils.getSession();
//		HttpSession session=request.getSession();
		
		session.setAttribute("mobileCheckCode", vcode);
		AuthenticationUtils sms = new AuthenticationUtils();
		
		if(sms.SendCode(mobile,vcode).getCode().equals("OK")) {
			

			info.put("state",true);
			return new ResponseEntity.Builder<Map<String, Boolean>>()
			  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
		}else {
			
			info.put("state",false);

			return new ResponseEntity.Builder<Map<String, Boolean>>()
			  	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
		}
		
		
	} catch (Exception e) {
		e.printStackTrace();
	
		info.put("state",false);

		return new ResponseEntity.Builder<Map<String, Boolean>>()
		  	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();
	}
		
}
	/*
	 * @Name:mobileCheckCode
	 * @Author:lvjisheng
	 * @Date:2018.09.06
	 */	
	@RequestMapping(value = "/mobileCheckCode", method = RequestMethod.POST)
	@ResponseBody
	@WriteLog(value="MobileCheckCode")
	public ResponseEntity<?> mobileCheckCode(@RequestBody AuthCode authCode) throws Exception {
		log.info("--------mobileCheckCode   start--------");

		String mobilecode = authCode.getMobileCode();
        
		Session session=ShiroUtils.getSession();
		log.info("mail hello" + mobilecode);
		log.info("mail hello" + session.getAttribute("mobileCheckCode"));
		
		Map<String,Boolean> info = new HashMap<>();
		
		if (mobilecode.equalsIgnoreCase((String) session.getAttribute("mobileCheckCode"))) {
			info.put("state",true);
			return new ResponseEntity.Builder<Map<String, Boolean>>()
			  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();

		} else {
			info.put("state",false);
			return new ResponseEntity.Builder<Map<String, Boolean>>()
			  	      .setData(info).setErrorCode(ErrorCode.GENERAL_ERROR).build();

		}
		
		
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

				mailInfo.settoAddress(userEmail);
				mailInfo.setfromAddress("3602745100@qq.com");
				mailInfo.setmailPassword("hgmwayjbwmomcjdd");
				mailInfo.setmailUsername("3602745100@qq.com");
				mailInfo.setmailSubject("密码重置");
	
				String vcode = SendMailUtils.createRandomVcode();
				log.info("vcode"+vcode);
				
				Session session = ShiroUtils.getSession();
				//HttpSession session=request.getSession(true);
				// 把当前生成的验证码存在session中，当用户输入后进行对比
				session.setAttribute("mailCheckCode", vcode);
	
				StringBuffer demo = new StringBuffer();
				demo.append("亲爱的：您好！<br><br>");
				demo.append("验证码：" + vcode);
	
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
}
