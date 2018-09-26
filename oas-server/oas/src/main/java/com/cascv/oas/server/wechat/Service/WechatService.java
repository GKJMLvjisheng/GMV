package com.cascv.oas.server.wechat.Service;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.mapper.EnergyWechatMapper;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.model.EnergyWechatModel;
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
	private EnergyWechatMapper energyWechatMapper;
	@Autowired
	private UserService userService;
    //private Map<String,Object> userInfo=new HashMap<String,Object>();
    //判断是否输入"获取验证码"
    Boolean isChecked=false;
    private static final Integer POWER_SOURCE_CODE_OF_OFFICIALACCOUNT = 3; 
    @SuppressWarnings("null")
	public String processRequest(HttpServletRequest request){
        Map<String, String> map = WechatMessageUtil.xmlToMap(request);
        // 发送方帐号（一个OpenID）
        String fromUserName = map.get("FromUserName");
        String openId=fromUserName;
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
        String userUuid="";
        String uuid="";
        //获取验证码
        for(int j = 0; j< 6; j++){
            result+=((int)((Math.random()*9+1)));
        }
        String eventType = map.get("Event");
        try {       
        // 对消息进行处理       
        if(WechatMessageUtil.MESSAGE_TEXT.equals(msgType)){
			        //判断回复的内容
			        if ("获取验证码".equals(map.get("Content"))) {
			        	responseContent="请输入OasDapp的登录账号\n";
			        	isChecked=true;
			            }
			        //根据用户名生成验证码
			        else if(isChecked){
			        	//判断用户名是否存在
			        	 if(userService.findUserByName(map.get("Content"))!=null){
			        		 userUuid=userService.findUserByName(map.get("Content")).getUuid();
			        		 //判断该微信号是否已经绑定了其他用户
			        		 if(energyWechatMapper.findWechatRecordByOpenid(openId)==null&&energyWechatMapper.findWechatRecordByUserUuid(userUuid)==null){
						        	log.info("正在获取验证码..");
						        	//将已获取验证码的用户状态进行绑定
						        	activityCompletionStatus=new ActivityCompletionStatus();
						            responseContent="用户"+map.get("Content")+"的验证码是:"+result+"\n";     
						            String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
						            log.info(userUuid);
						            activityCompletionStatus.setUserUuid(userUuid);
						            activityCompletionStatus.setSourceCode(POWER_SOURCE_CODE_OF_OFFICIALACCOUNT);
						            //未使用表示1
						            activityCompletionStatus.setStatus(0);
						            uuid=UuidUtils.getPrefixUUID(UuidPrefix.ACTIVITY_COMPLETION_STATUS);
						            activityCompletionStatus.setUuid(uuid);
						            activityCompletionStatus.setCreated(now);;
						            energySourcePowerMapper.insertActivity(activityCompletionStatus);
						            userModel.setName(map.get("Content"));
						            userModel.setIdentifyCode(Integer.valueOf(result));
						            userService.updateIdentifyCode(userModel);
						            
						            //进行微信与OasDapp用户的绑定
						            EnergyWechatModel energyWechatModel=new EnergyWechatModel();			            						            
						            log.info("uuid{}",UuidUtils.getPrefixUUID(UuidPrefix.WECHAT_STATUS));
						            energyWechatModel.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.WECHAT_STATUS));
						            energyWechatModel.setUserUuid(userUuid);
						            energyWechatModel.setWechatOpenid(openId);
						            energyWechatModel.setCreated(now);
						            energyWechatMapper.insertWechatRecord(energyWechatModel);
						            log.info("***end***"); 
						            isChecked=false;
			        		 }else if(energyWechatMapper.findWechatRecordByUserUuid(userUuid)!=null){
			        			    String oldOpenId=energyWechatMapper.findWechatRecordByUserUuid(userUuid).getWechatOpenid();
			        			   if(oldOpenId.equals(openId)) {
					        	        Integer idenfyCode=userService.findUserByName(map.get("Content")).getIdentifyCode();
							        	responseContent="用户"+map.get("Content")+"的验证码是:"+idenfyCode.toString()+"\n"; 
					        	         log.info("该微信号绑定了当前用户!"); 
					        	         isChecked=false;
			        	         }else{
			        	        	 responseContent="该用户已经绑定了其他微信号!\n"; 
			        	        	 log.info("该微信号已经绑定了其他用户!"); 
			        	        	 isChecked=false;
			        	         }
			        		 }
			        		 else{
			        			 //responseContent="用户"+map.get("Content")+"已经绑定了其他微信号\n";
			        			 responseContent="你已经绑定了用户，每个微信号只能绑定一个OasDapp账号!\n";
			        			 log.info("该微信号已经绑定了其他用户!"); 
			        			 isChecked=false;
			        		     }
			        		
			        	 }else{  
			        		     responseContent="用户名不存在!\n";
			        		     log.info("用户名不存在!");
			        		     isChecked=false;
			        	      }			     
			        }    
			        
			        else {
			            responseContent="您的输入有误!如需获取验证码,请按照以下提示输入:'获取验证码'";
			             }   	
			        }
		        else if (eventType.equals(WechatMessageUtil.MESSAGE_EVENT_SUBSCRIBE)) {//如果用户发送的是event类型的消息
		            //responseContent="欢迎国科云景的小伙伴们!\n"+"输入'获取验证码'即可获得相应的验证码来提升算力";
		        	responseContent="你好，欢迎关注oaseschain!";
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
        	log.info(e.getMessage());
        	e.getStackTrace();
        }
        return responseMessage;   
   }
}
