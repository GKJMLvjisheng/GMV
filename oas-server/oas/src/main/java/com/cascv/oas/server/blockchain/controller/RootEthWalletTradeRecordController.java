package com.cascv.oas.server.blockchain.controller;
/**
 * @author Ming Yang
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootEthWalletTradeRecordController {
	
	@GetMapping("/wallet/userWallet")
	public String userWallet() {
	    return "wallet/userWallet";  
	    }

	@GetMapping("/wallet/ethWallet")
	public String ethWallet() {
	    return "wallet/ethWallet";  
	    }

	@GetMapping("/wallet/energyWallet")
	public String eneryWallet() {
	    return "wallet/energyWallet";  
	    }
	
}
