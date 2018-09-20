package com.cascv.oas.server.wechat.Service;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.wechat.Utils.WechatMessageUtil;
import com.cascv.oas.server.wechat.vo.TextMessage;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class WechatService {
	@Autowired
	private EnergySourcePowerMapper energySourcePowerMapper;
	@Autowired
	private UserService userService;
    //private Map<String,Object> userInfo=new HashMap<String,Object>();
    //判断是否输入"获取验证码"
    Boolean isChecked=false;
    private static final Integer POWER_SOURCE_CODE_OF_OFFICIALACCOUNT = 3; 
    public String processRequest(HttpServletRequest request){
        Map<String, String> map = WechatMessageUtil.xmlToMap(request);
        // 发送方帐号（一个OpenID）
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        // 消息类型
        String msgType = map.get("MsgType");
        // 默认回复一个"success"
        String responseMessage = "success";
        String responseContent = "初始化信息";
        //生成随机数
        String result="";
        ActivityCompletionStatus activityCompletionStatus=new ActivityCompletionStatus();
        UserModel userModel=new UserModel();
        
        //获取验证码
        for(int j = 0; j< 6; j++){
            result+=((int)((Math.random()*9+1)));
        }
        String eventType = map.get("Event");
        try {       
        // 对消息进行处理       
        if (WechatMessageUtil.MESSAGE_TEXT.equals(msgType)) {
        	//*****可能会报错
        	String userUuid=userService.findUserByName(map.get("Content")).getUuid();
        	activityCompletionStatus=energySourcePowerMapper.selectByUserUuid(userUuid);
        	//******
            //判断回复的内容
            if ("获取验证码".equals(map.get("Content"))) {
            	responseContent="请输入OasDapp的登录账号\n";
            	isChecked=true;
            }
            //根据用户名生成验证码
            else if(isChecked&& activityCompletionStatus==null){
            	log.info("正在获取验证码..");
                responseContent="用户"+map.get("Content")+"的验证码是:"+result+"\n";
                isChecked=false;

                activityCompletionStatus.setUserUuid(userUuid);
                activityCompletionStatus.setSource_code(POWER_SOURCE_CODE_OF_OFFICIALACCOUNT);
                //未使用表示1
                activityCompletionStatus.setStatus(0);
                energySourcePowerMapper.insertActivity(activityCompletionStatus);
                userModel.setName(map.get("Content"));
                userModel.setIdentifyCode(Integer.valueOf(result));;
                userService.updateIdentifyCode(userModel);
            } 
            
			/**
			 * 判断输入的用户名是否存在（日后再改进）
			 */
            
            else if(activityCompletionStatus!=null){
            	log.info("你重复输入了..");
                responseContent="每个用户只能使用一次验证码来提升算力！";
            } 
            else {
                responseContent="您的输入有误!请重新输入:'获取验证码'";
            }   	
        }
        else if (eventType.equals(WechatMessageUtil.MESSAGE_EVENT_SUBSCRIBE)) {//如果用户发送的是event类型的消息
            responseContent="欢迎国科云景的小伙伴们!\n"+"输入'获取验证码'即可获得相应的验证码来提升算力";
        }
        else if ("unsubscribe".equals(WechatMessageUtil.MESSAGE_EVENT_UNSUBSCRIBE)) {
        	log.info("用户:"+toUserName+"已经取消了关注！");
        }      
        TextMessage textMessage = new TextMessage();
        textMessage.setMsgType(WechatMessageUtil.MESSAGE_TEXT);
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setContent(responseContent);
        responseMessage = WechatMessageUtil.textMessageToXml(textMessage);
        System.out.print(responseMessage);
        }catch(Exception e){
        	log.info("error");
        }
        return responseMessage;   
   }
//    public Map<String,Object> inquireUserInfo() {
//    	return userInfo;
//    }
}
