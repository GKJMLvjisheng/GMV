package com.cascv.oas.server.utils;

import java.util.Properties;
import java.util.concurrent.Callable;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.cascv.oas.server.user.model.MailInfo;

/*
 * Name:SendMailUtils
 * Author:lvjisheng
 * Date:2018.09.05
 */
public class SendMailUtils implements Callable<String> {
	private String hostqq = "smtp.qq.com";
    private String host163 = "smtp.163.com";
    
    private MailInfo mailInfo;
   
    public  SendMailUtils(MailInfo mailInfo){
        this.mailInfo = mailInfo;
    }
    

    /* 
     * @see java.lang.Thread#run()
     */
    @Override
    public String call() throws Exception {
    	String host=null;
    	try{
            Properties prop = new Properties();
            if(mailInfo.getfromAddress().indexOf("qq")!=-1)
            {host=hostqq;}
            else
            {host=host163;}
            
            //用QQ企业邮箱（海外版服务器）
            host="hwsmtp.exmail.qq.com";
            
            prop.setProperty("mail.host", host);
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");
            
            Session session = Session.getInstance(prop);
            session.setDebug(true);
            Transport ts = session.getTransport();
            ts.connect(host, mailInfo.getfromAddress(), mailInfo.getmailPassword());
            
            Message message = null;
            
            //message = createEmail(session,mailInfo);
            message = createhtmlMail(session,mailInfo);
            
            ts.sendMessage(message, message.getAllRecipients());
           
            ts.close();
           
            return "test result";
        }catch (Exception e) {
            throw new RuntimeException(e);   
        }
    }
    public Message createEmail(Session session,MailInfo mailInfo) throws Exception{
        
        MimeMessage message = new MimeMessage(session);
        
        //防止成为垃圾邮件，披上outlook的马甲
        message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
        
        message.setFrom(new InternetAddress(mailInfo.getfromAddress(),mailInfo.getmailUsername(), "UTF-8"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.gettoAddress()));
        message.setSubject(mailInfo.getmailSubject());
        
        String info = mailInfo.getmailSubject();
//       //将邮件的优先级别设置到最高，防止邮件丢入到垃圾箱
//        Message.Priority = System.Net.Mail.MailPriority.High;
       
        message.setContent(info, "text/html;charset=UTF-8");
        message.saveChanges();
        return message;
    }

    
    public Message createhtmlMail(Session session,MailInfo mailInfo) throws Exception{
    	MimeMessage message = new MimeMessage(session);
        
    	 //防止成为垃圾邮件，披上outlook的马甲
        message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
    	
        message.setFrom(new InternetAddress(mailInfo.getfromAddress(),mailInfo.getmailUsername(), "UTF-8"));

        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(mailInfo.gettoAddress(), "youyouyu", "UTF-8"));

        message.setSubject(mailInfo.getmailSubject());
      //不被当作垃圾邮件的关键代码--Begin            

        message.addHeader("X-Priority", "3");            

        message.addHeader("X-MSMail-Priority", "Normal");            

        message.addHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869");   //本文以outlook名义发送邮件，不会被当作垃圾邮件            

        message.addHeader("X-MimeOLE", "Produced By Microsoft MimeOLE V6.00.2900.2869");            

        message.addHeader("ReturnReceipt", "1");            

        //不被当作垃圾邮件的关键代码--End 
        String info = mailInfo.getmailContent();
       
        //String htmlText = "<H1>"+info+"</H1>" + "<p>"+info+"</p>" + "<a href='http://www.3lian.com/gif/2016/12-15/143678_5.html'/>Link text</a>";
        String htmlText = "<H1>"+info+"</H1>";
        message.setContent(htmlText, "text/html;charset=UTF-8");
        return message;
    }
    

    public static String createRandomVcode(){
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }
    
}
