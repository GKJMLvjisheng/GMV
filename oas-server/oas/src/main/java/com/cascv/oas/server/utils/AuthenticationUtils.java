package com.cascv.oas.server.utils;

import com.cascv.oas.server.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.cascv.oas.server.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

/*
 * Name:AuthenticationUtils
 * Author:lvjisheng
 * Date:2018.09.05
 */
public class AuthenticationUtils {
	
//	public boolean checkState() {
//		boolean flag = false;
//		return flag;
//	}
	
	public static String createRandomVcode(){
        String vcode = "";
        for (int i = 0; i < 6; i++) {
        	vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }
	
	//public String SendCode(String PhoneNumbers, String mobilecode ) throws ClientException{
	public  SendSmsResponse SendCode(String PhoneNumbers, String mobilecode ) throws ClientException{
        
		System.out.println("*********SendCode start********************");
		
		
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        
        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";
  
        final String accessKeyId = "LTAIL9r6XmpbBe9g";
        final String accessKeySecret = "Ra4rhaRCGsleSxOQHb9BNQTPCAgMfC";
        
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product,
                domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
      
        SendSmsRequest request = new SendSmsRequest();
    
        request.setMethod(MethodType.POST);
        
        request.setPhoneNumbers(PhoneNumbers);
       
        request.setSignName("国科云景");
        
        request.setTemplateCode("SMS_143716732");
        
        request.setTemplateParam("{\"code\":\"" + mobilecode + "\"}");
        System.out.println("***********mobilecode = ************" + mobilecode);
      
         SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
         if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
 
            System.out.println(sendSmsResponse.getMessage());
         }
         else {
        	 System.out.println(sendSmsResponse.getMessage());
         }
         
         return sendSmsResponse;
     }	
	       

}
