package com.cascv.oas.server.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootActivityController {
	
	@GetMapping(value="/activityReward/activityReward")
	public String activityReward(){
		
	    return "activityReward/activityReward";
	}

}
