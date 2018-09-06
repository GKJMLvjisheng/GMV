package com.cascv.oas.server.user.model;

public class MailInfo {
	private String fromAddress; 
    private String toAddress; 
    private String mailPassword; 
    private String mailUsername; 
    private String mailSubject;  
    private String mailContent;  

    
    public String getfromAddress() {
    	
        return fromAddress;
    }
    public void setfromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
    public void settoAddress(String toAddress) {
        this.toAddress = toAddress;
    }
    public String gettoAddress() {
    	
        return toAddress;
    }
    public String getmailPassword() {
        return mailPassword;
    }
    public void setmailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }
    public String getmailUsername() {
        return mailUsername;
    }
    public void setmailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }
    public String getmailSubject() {
        return mailSubject;
    }
    public void setmailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }
    public String getmailContent() {
        return mailContent;
    }
    public void setmailContent(String mailContent) {
        this.mailContent = mailContent;
    }

}
