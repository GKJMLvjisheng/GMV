package com.gkyj.gmv.server.test.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.gkyj.gmv.server.load.config.MediaServer;
import lombok.extern.slf4j.Slf4j;
import com.gkyj.gmv.server.log.annocation.WriteLog;
import com.gkyj.gmv.server.test.model.TestModel;
import com.gkyj.gmv.server.test.service.TestService;
import com.gkyj.gmv.server.test.wrapper.LoadInfo;

@RestController
@RequestMapping(value = "/api/v1/test")
@Slf4j
public class TestController {
	
	@Autowired
	private TestService testService;
	@Autowired
	private MediaServer mediaServer;
	/**
	 * @category 查询最后一圈n秒内的数据
	 * @param (loadInfo),startTime,endTime
	 * @return 
	 */
	@PostMapping(value = "/selectTestMsg")  
	@ResponseBody
	public ResponseEntity<?> selectTestMsg(@RequestBody LoadInfo loadInfo){

	    Integer number=loadInfo.getNumber()*4;
	    //初始化几秒的数据
	    List<TestModel> testModelList = testService.selectLoadMsg(number);
	    
	    for (TestModel testModel : testModelList) {
			String fullLink = mediaServer.getImageHost() + testModel.getPicPath();
			testModel.setPicPath(fullLink);
		  }

		    LoadInfo putLoadInfo = new LoadInfo();
	        putLoadInfo.setTestModelList(testModelList);
		    List<TestModel> modelList=testService.selectLoad();
	        //获取初始化开始和结束时间
		    String startTime = modelList.get(0).getTime();
		    putLoadInfo.setStartTime(startTime);
		    Integer listSize = modelList.size();
		    String endTime = modelList.get(listSize-1).getTime();
		    putLoadInfo.setEndTime(endTime);
            putLoadInfo.setTotal(testModelList.size());
		return new ResponseEntity.Builder<LoadInfo>()
				.setData(putLoadInfo)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	
	/**
	 * @category 根据时间段去查询载体数据
	 * @param  loadInfo
	 * @return 
	 * @throws ParseException 
	 */
	@PostMapping(value = "/selectTestMsgByPeriod")
	@ResponseBody
	@WriteLog(value="selectTestMsgByPeriod")
	public ResponseEntity<?> selectTestMsgByPeriod(@RequestBody LoadInfo loadInfo) throws ParseException{


		String startTime = loadInfo.getStartTime();
		String endTime3 = loadInfo.getEndTime();
		Date endTime1 = null;
		String endTime = null;

		//开始时间加2秒--字符串转Date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		endTime1 = format1.parse(startTime);  	//将字符串解析为日期类型
		log.info("当前时间={}" ,sdf.format(endTime1));  //将日期类型解析为字符串
		Date afterTime3 = new Date(endTime1 .getTime() + 2000);  //getTime(),返回日期类型的毫秒数
		log.info("处理后时间+2={}" ,sdf.format(afterTime3));

		//Date转字符串
		String endTime2 = sdf.format(afterTime3);

		if(endTime3==null) {
			endTime = endTime2; //前端输入开始时间加3秒=结束时间
		}else {
			endTime = endTime3; //前端输入的结束时间
		}

		List<TestModel> testModelList = testService.selectLoadMsgByPeriod(startTime,endTime);
		for (TestModel testModel : testModelList) {
			String fullLink = mediaServer.getImageHost() + testModel.getPicPath();
			testModel.setPicPath(fullLink);
		}
		LoadInfo putLoadInfo = new LoadInfo();
		putLoadInfo.setTestModelList(testModelList);
		putLoadInfo.setTotal(testModelList.size());
		return new ResponseEntity.Builder<LoadInfo>()
				.setData(putLoadInfo)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}

}

