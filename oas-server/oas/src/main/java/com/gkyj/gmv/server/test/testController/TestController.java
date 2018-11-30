package com.gkyj.gmv.server.test.testController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gkyj.gmv.server.test.testMapper.TestMapper;
import com.gkyj.gmv.server.test.testModel.TestModel;

import io.swagger.annotations.Api;

@RestController
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")
public class TestController {
	@Autowired
	private TestMapper testMapper;  
	@RequestMapping("/list")
	public List<TestModel> list() {
		return testMapper.getAll();
	}
}
