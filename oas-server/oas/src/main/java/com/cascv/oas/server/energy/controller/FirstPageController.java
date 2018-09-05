package com.cascv.oas.server.energy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.FirstPageReturn;

@RestController
@RequestMapping(value = "/api/v1/energyPoint")
public class FirstPageController {
	
	@Autowired
    private EnergyService energyService;
	
	@PostMapping(value = "/firstPageReturn")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> firstPageReturn(@RequestBody FirstPageReturn firstPageReturn){
		
		return null;
	}

}
