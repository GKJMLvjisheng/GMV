package com.cascv.oas.server.news.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class RootNewsController {

// NewsManage 
@GetMapping("/newsManage")

public String newsManage() {
	    return "newsManage";  
	    }

@GetMapping(value="/newInformation")
	public String newInformation() {
		return "newInformation";
	}

@GetMapping("/QA/QAManage")
public String QAManage() {
	    return "QA/QAManage";  
	    }
@GetMapping("/withdrawMoney/brokerageConfig")
public String brokerageConfig() {
	    return "withdrawMoney/brokerageConfig";  
	    }
@GetMapping("/withdrawMoney/requestAudit")
public String auditing() {
	    return "withdrawMoney/requestAudit";  
	    }
@GetMapping("/KYC/KYC")
public String KYC() {
	    return "KYC/KYC";  
	    }
@GetMapping("/KYC/limitConfig")
public String limitConfig() {
	    return "KYC/limitConfig";  
	    }
}