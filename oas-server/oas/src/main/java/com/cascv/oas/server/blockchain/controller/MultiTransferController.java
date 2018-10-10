package com.cascv.oas.server.blockchain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/")
public class MultiTransferController {
	
	@RequestMapping(value="/multiTransfer")
	public String multiTransfer() {
		return "multiTransfer";
	}
	@RequestMapping(value="/netConfiguration/netConfig")
	public String netConfig() {
		return "netConfiguration/netConfig";
	}

}
