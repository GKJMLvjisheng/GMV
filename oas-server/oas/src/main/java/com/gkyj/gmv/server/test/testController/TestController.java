package com.gkyj.gmv.server.test.testController;

import java.util.List;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.gkyj.gmv.server.test.testModel.TestModel;
import com.gkyj.gmv.server.test.testService.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;

@RestController
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")
@Slf4j
public class TestController {
	@Autowired
	private TestService testService;

	@RequestMapping("/list")
	@ResponseBody
	public ResponseEntity<?> list(String time) {
		List<TestModel> testModelList= testService.findByTime(time);
		log.info("end");
		return new ResponseEntity.Builder<List<TestModel>>()
				.setData(testModelList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
}
