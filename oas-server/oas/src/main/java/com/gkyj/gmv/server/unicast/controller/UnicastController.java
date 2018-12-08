package com.gkyj.gmv.server.unicast.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;

import io.swagger.annotations.Api;

@RestController
@Api(value="Unicast Interface")
@RequestMapping(value="/api/v1/unicast")
public class UnicastController {
	@PostMapping(value = "/sendTestVideo")  
	@ResponseBody
	public ResponseEntity<?> sendTestVideo(){
		String url = "http://10.0.0.88:9090/CassandraVideo/CassandraTest.mp4";
		return new ResponseEntity.Builder<String>()
                .setData(url)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
	}
}