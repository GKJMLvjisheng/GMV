package com.cascv.oas.server.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
@Service 
public class MessageService {
    private Map<String, MessageAttributeValue> smsAttributes;


    public Map<String, MessageAttributeValue> getDefaultSMSAttributes() {
        if (smsAttributes == null) {
            smsAttributes = new HashMap<>();
            smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                    .withStringValue("1")
                    .withDataType("String"));
            smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                    .withStringValue("0.05")
                    .withDataType("Number"));
            smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                    .withStringValue("Transactional")
                    .withDataType("String"));
        }
        return smsAttributes;
    }

    public PublishResult sendSMSMessage(String phoneNumber, String message) {
        return sendSMSMessage(phoneNumber, message, getDefaultSMSAttributes());
    }

    public PublishResult sendSMSMessage(String phoneNumber, String message, Map<String, MessageAttributeValue> smsAttributes) {
    	//匿名类
        AWSCredentials awsCredentials = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId(){
                return "AKIAJOW26VQBDT6ZP23Q"; // 带有发短信权限的 IAM的 ACCESS_KEY
            }

            @Override
            public String getAWSSecretKey(){
                return "ca/G/RtXQR9Sto7AEoKebsiiAb2rTIgl/Hakl0aY"; //带有发短信权限的IAM的 SECRET_KEY
            }
        };
        
        AWSCredentialsProvider provider = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return awsCredentials;
            }

            @Override
            public void refresh() {
            }
        };
        AmazonSNS amazonSNS = null;
        try {
            amazonSNS = AmazonSNSClientBuilder.standard().withCredentials(provider).withRegion("ap-northeast-1").build();
        } catch (Exception e) {

        }
        return amazonSNS.publish(
                new PublishRequest()
                        .withMessage(message)
                        .withPhoneNumber(phoneNumber)
                        .withMessageAttributes(smsAttributes)
        );
    }
    
	public static String createRandomVcode(){
        String vcode = "";
        for (int i = 0; i < 6; i++) {
        	vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }
   
}
