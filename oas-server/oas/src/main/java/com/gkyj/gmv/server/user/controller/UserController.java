package com.gkyj.gmv.server.user.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.gkyj.gmv.server.user.model.TestData;
import com.gkyj.gmv.server.user.model.UserModel;
import com.gkyj.gmv.server.user.service.UserService;
import com.gkyj.gmv.server.user.wrapper.LoginResult;
import com.gkyj.gmv.server.user.wrapper.LoginVo;
import com.gkyj.gmv.server.utils.ShiroUtils;

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
  private KafkaTemplate kafkaTemplate;
    
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
	


	// Destroy
	@ApiOperation(value="Destroy", notes="")
	@PostMapping(value="/destroy")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> destroy() {
		UserModel userModel = ShiroUtils.getUser();
		String uuid = userModel.getUuid();
		userService.deleteUserByUuid(uuid);
		return new ResponseEntity.Builder<Integer>()
					.setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	}
	
	@RequestMapping(value = "/sendKafkaInfo/{topicName}", method = RequestMethod.POST,consumes = "application/json") 
	public void sendKafka(@RequestBody String message,@PathVariable(value = "topicName") String topicName) {
        try {
        	if(topicName == null) return;
        	JSONObject jb = JSON.parseObject(message);
        	String jbMessage = (String)jb.get("value");
        	if(jbMessage != null && !jbMessage.isEmpty()) {
        		kafkaTemplate.send(topicName,"str", jbMessage);
        		return;
        	}
        	List<TestData> list = new ArrayList<>();
        	String date = DateUtils.getTime();
        	for(int i=0;i<4;i++) {
        		TestData a = new TestData();
        		String [] picImgs = {"https://oas.cascv.com/image/news/201810290521162072513786-face09877.jpg","https://oas.cascv.com/image/news/20181029052104493915232-007.jpg"};
        		switch(i) {
	    			case 0:
	    				a.setMinerName("温度");
	    				a.setMinerDescription(new BigDecimal(randomNumber(38,20)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
	    				break;
	    			case 1:
	    				a.setMinerName("湿度");
	    				a.setMinerDescription(new BigDecimal(randomNumber(50,30)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
	    				break;
	    			case 2:
	    				a.setMinerName("电压");
	    				a.setMinerDescription(new BigDecimal(randomNumber(220,36)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
	    				break;
	    			case 3:
	    				a.setMinerName("电流");
	    				a.setMinerDescription(new BigDecimal(randomNumber(1,5)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
	    				break;
        		}
        		int index =(int)(Math.random()*2);
        		a.setLoadPicturePath(picImgs[index]);
            	a.setUpdated(date);
            	list.add(a);
        	}
        	
        	kafkaTemplate.send(topicName, "list" ,list);
        } catch (Exception e) {
        	log.error("发送kafka失败", e);
        }
    }
	
	private String randomNumber(int x,int y) {
		return String.valueOf(Math.random() * (x - y + 1) + y);
	}
		
}
