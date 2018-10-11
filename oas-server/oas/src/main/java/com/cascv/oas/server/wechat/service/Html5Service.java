package com.cascv.oas.server.wechat.Service;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergyWechatMapper;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.model.EnergyWechatModel;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.wechat.Utils.GetOpenIdUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
@Slf4j
@Service
public class Html5Service{
//	EventMessage em = EventMessage.LoadFromXml(RequestXml); 
	@Autowired
	private ActivityMapper activityMapper;	
	@Autowired
	private EnergyWechatMapper energyWechatMapper;
	@Autowired
	private UserService userService;
    //private Map<String,Object> userInfo=new HashMap<String,Object>();
    //判断是否输入"获取验证码"
    private static final Integer POWER_SOURCE_CODE_OF_OFFICIALACCOUNT = 3;        
	public Map<String,String> processRequest(HttpServletRequest request){
		Map<String,String> info=new HashMap<>();
    	String name=request.getParameter("name"); 
        String result="";
        String userUuid="";
        String uuid="";
        String responseContent="";
        /**
         * 获取用户的OpenId
         */
        String openId="";
        
//       GetOpenIdUtil getopenId=new GetOpenIdUtil();
       //调用访问微信服务器工具方法，传入三个参数获取带有openid、session_key的json字符串
//       String jsonId=getopenId.getOpenId(appId,code,secret);
//       log.info("jsonId={}",jsonId);
//       JSONObject jsonObject = JSONObject.fromObject(jsonId); 
       //从json字符串获取openid和session_key
       //注意函数内部写的是openid，因此这里也需要和其相对应
//       String openId=jsonObject.getString("openid");
//       log.info("openId={}",openId);
       //String session_key=jsonObject.getString("session_key");
       
        //生成随机数
        ActivityCompletionStatus activityCompletionStatus=new ActivityCompletionStatus();
        UserModel userModel=new UserModel();

        //获取验证码
        for(int j = 0; j< 6; j++){
            result+=((int)((Math.random()*9+1)));
        }
        try {       
        // 对消息进行处理       
			        	//判断用户名是否存在
			        	 if(userService.findUserByName(name)!=null){
			        		 userUuid=userService.findUserByName(name).getUuid();
			        		 //判断该微信号是否已经绑定了其他用户
			        		 if(energyWechatMapper.findWechatRecordByOpenid(openId)==null&&energyWechatMapper.findWechatRecordByUserUuid(userUuid)==null){
						        	log.info("正在获取验证码..");
						        	//将已获取验证码的用户状态进行绑定
						        	activityCompletionStatus=new ActivityCompletionStatus();
						            responseContent="用户"+name+"的验证码是:"+result+"\n";     
						            String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
						            log.info(userUuid);
						            activityCompletionStatus.setUserUuid(userUuid);
						            activityCompletionStatus.setSourceCode(POWER_SOURCE_CODE_OF_OFFICIALACCOUNT);
						            //未使用表示1
						            activityCompletionStatus.setStatus(0);
						            uuid=UuidUtils.getPrefixUUID(UuidPrefix.ACTIVITY_COMPLETION_STATUS);
						            activityCompletionStatus.setUuid(uuid);
						            activityCompletionStatus.setCreated(now);
						            activityCompletionStatus.setUpdated(now);
						            activityMapper.insertActivityCompletionStatus(activityCompletionStatus);
						            userModel.setName(name);
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
						            info.put("responseContent",responseContent);
						            info.put("state","success");
			        		 }else if(energyWechatMapper.findWechatRecordByUserUuid(userUuid)!=null){
			        			    String oldOpenId=energyWechatMapper.findWechatRecordByUserUuid(userUuid).getWechatOpenid();
			        			   if(oldOpenId.equals(openId)) {
					        	        Integer idenfyCode=userService.findUserByName(name).getIdentifyCode();
							        	responseContent="用户"+name+"的验证码是:"+idenfyCode.toString()+"\n"; 
					        	         log.info("该微信号绑定了当前用户!"); 
					        	         info.put("responseContent",responseContent);
								         info.put("state","success");
			        	           }else{
			        	        	 responseContent="该用户已经绑定了其他微信号!\n"; 
			        	        	 log.info("该微信号已经绑定了其他用户!"); 
				        	         info.put("responseContent",responseContent);
							         info.put("state","failure");
			        	           }
			        		   }
			        		 else{
			        			 responseContent="你已经绑定了用户，每个微信号只能绑定一个OasDapp账号!\n";
			        			 log.info("该微信号已经绑定了其他用户!"); 
			        	         info.put("responseContent",responseContent);
						         info.put("state","failure");
			        		     }
			        		
			        	 }else{  
			        		     responseContent="用户名不存在!\n";
			        		     log.info("用户名不存在!");
			        	         info.put("responseContent",responseContent);
						         info.put("state","failure");
			        	      }			          				         
        }catch(Exception e){
        	log.info("error");
        	log.info(e.getMessage());
        	e.getStackTrace();
        }
        return info;   
   }
}
