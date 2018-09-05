package com.cascv.oas.server.user.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
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
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.service.EnergyPointService;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.user.wrapper.LoginResult;
import com.cascv.oas.server.user.wrapper.LoginVo;
import com.cascv.oas.server.user.wrapper.RegisterConfirm;
import com.cascv.oas.server.user.wrapper.RegisterResult;
import com.cascv.oas.server.utils.FileUtils;
import com.cascv.oas.server.utils.HostIpUtils;
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
	private EthWalletService ethWalletService;
	
	@Autowired
	private UserWalletService userWalletService;
	
	@Autowired
	private EnergyPointService energyPointService;
  
	@ApiOperation(value="Login", notes="")
	@PostMapping(value="/login")
	@ResponseBody
	public ResponseEntity<?> userLogin(@RequestBody LoginVo loginVo) {
		log.info("authentication name {}, password {}", loginVo.getName(), loginVo.getPassword());
		Boolean rememberMe = loginVo.getRememberMe() == null ? false : loginVo.getRememberMe();
		UsernamePasswordToken token = new UsernamePasswordToken(loginVo.getName(), loginVo.getPassword(), rememberMe);
      LoginResult loginResult = new LoginResult();
	    Subject subject = SecurityUtils.getSubject();
      try {
          subject.login(token);
          loginResult.setToken(ShiroUtils.getSessionId());
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
//  @Transactional
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
		log.info("invideCode {}", userModel.getInviteCode());
	  info.put("name", userModel.getName());
	  info.put("nickname", userModel.getNickname());
	  info.put("inviteCode", userModel.getInviteCode().toString());
	  info.put("gender", userModel.getGender());
	  info.put("address", userModel.getAddress());
	  info.put("birthday", userModel.getBirthday());
	  info.put("email", userModel.getEmail());
	  info.put("mobile", userModel.getMobile());
	  return new ResponseEntity.Builder<Map<String, String>>()
	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();	  
	}
	
	/*
	 * Name:upadateUserInfo
	 * Author:lvjisheng
	 * Date:2018.09.03
	 */
	@PostMapping(value="/updateUserInfo")
	@ResponseBody
	public ResponseEntity<?> updateUserInfo(@RequestBody updateUserInfo userInfo){
	  Map<String,String> info = new HashMap<>();
	  UserModel userModel=new UserModel();   
      try {
    	 log.info("userUUID {}",userInfo.getName());
		 userModel.setName(ShiroUtils.getUser().getName());
		 userModel.setNickname(userInfo.getNickname());
	     userModel.setGender(userInfo.getGender());
	     userModel.setBirthday(userInfo.getBirthday());
	     userModel.setAddress(userInfo.getAddress());
    	 userService.updateUser(userModel); 
    	 UserModel userNewModel=new UserModel();
    	 userNewModel=userService.findUserByName(ShiroUtils.getUser().getName());
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
      }catch(Exception e){
    	  log.info("修改失败"+e);
      }
	       
	  return new ResponseEntity.Builder<Map<String, String>>()
	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	}
	
	/*
	 * Name:upLoadImg
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	@RequestMapping(value="/upLoadImg", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	public ResponseEntity<?> upLoadImg(@RequestParam("file") MultipartFile file,UserModel userModel)
	{
		log.info("doUpLoadImg-->start");
		Map<String,String> info=new HashMap<>();
	    //String contentType = file.getContentType();
	    //生成唯一的文件名
	    String fileName = UUID.randomUUID().toString().replaceAll("-", "")+"-"+file.getOriginalFilename();
	    log.info("fileName-->{}" + fileName);
	    //本地存储图片的路径
	    String filePath="D:/Temp/Image/profile/";
	    try {	    	
	        FileUtils.uploadFile(file.getBytes(), filePath, fileName);
	        log.info("imgPath-->{}" + filePath+fileName);
	        //获取本地的IP地址
	        String hostIp=HostIpUtils.getHostIp();
	        String proPath="http://"+hostIp+":8080/image/profile/"+fileName;
	        log.info("proPath-->{}" + proPath);
	        //需要根据ID进行修改
	        UserModel userNewModel=new UserModel();
	        String profile=filePath+fileName;
	        //获取用户名
	        String name=userModel.getName();
	        log.info("---userName-->{}" +name);
	        userNewModel.setName(name);
	        userNewModel.setProfile(profile);
	        userService.updateUserProfile(userNewModel);
	        //图片存储的相对路径
	    	info.put("ImageUrl",proPath);
	    	log.info("UpLoadSuccuss-->");
	    } catch (Exception e){
	    	log.info("UpLoadFailed-->");
	    }
        return new ResponseEntity.Builder<Map<String, String>>()
	      	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	}
	/*
	 * Name:resetMobile
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	@RequestMapping(value = "/resetMobile", method = RequestMethod.POST)
	public ResponseEntity<?> resetMobile(@RequestBody UserModel userModel) throws Exception {
    Map<String,String> info=new HashMap<>();
	String name = userModel.getName();
	String mobile = userModel.getMobile();
	log.info("************ResetMobile start****************");

	log.info("userNickname =" + name);

	UserModel userNewModel = new UserModel();

	userNewModel.setMobile(mobile);

	userNewModel.setName(name);

	userService.resetMobileByName(userNewModel);

	log.info("userMobile ={}",userNewModel.getMobile());

	log.info("--------ResetMobile end-------");

	return new ResponseEntity.Builder<Map<String, String>>()
    	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	}
	
	/*
	 * Name:resetMail
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	@RequestMapping(value = "/resetMail", method = RequestMethod.POST)
	public ResponseEntity<?> resetMail(@RequestBody UserModel userModel) throws Exception {

    Map<String,String> info=new HashMap<>();
	String name = userModel.getName();
	String email = userModel.getEmail();
	log.info("************ResetMail start****************");
	log.info("userMail-->"+email);
	UserModel userNewModel = new UserModel();

	userNewModel.setEmail(email);

	userModel.setName(name);

	userService.resetEmailByName(userModel);

	log.info("--------end-------");

	return new ResponseEntity.Builder<Map<String, String>>()
  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	}
	/*
	 * Name:sendMobile
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	@RequestMapping(value = "/sendMobile", method = RequestMethod.POST)
	public ResponseEntity<?> sendMobile(@RequestBody UserModel userModel) throws Exception {

    Map<String,String> info=new HashMap<>();
	log.info("-----------sendMobile start---------------");
//	String mobileAndMail = request.getParameter("mobileAndMail");
//	
//	Map<String, Object> map = new HashMap<>();
//	boolean state = false;
//	
//	String userMobile = mobileAndMail;	
//	try {
//	
//		String vcode = MobileAuthentication.createRandomVcode();
//		System.out.println("vcode = "+vcode);
//		HttpSession session = request.getSession(true);
//		
//		session.setAttribute("mobileCheckCode", vcode);
//
//		MobileAuthentication sms = new MobileAuthentication();
//		
//		if(sms.SendCode(userMobile,vcode).getCode().equals("OK")) {
//			
//			state = true;
//			
//		}else {
//			
//			state = false;
//		}
//		
//		
//	} catch (Exception e) {
//		e.printStackTrace();
//		state = false;
//	}
//	
//	 map.put("state",state);
//	 System.out.println(map);
		return new ResponseEntity.Builder<Map<String, String>>()
		  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
}
}
