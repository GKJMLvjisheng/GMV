package com.gkyj.gmv.server.load.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gkyj.gmv.server.load.service.LoadService;
import com.gkyj.gmv.server.load.wrapper.LoadInfo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.gkyj.gmv.server.load.config.MediaServer;
import com.gkyj.gmv.server.load.model.LoadModel;
import lombok.extern.slf4j.Slf4j;
import com.gkyj.gmv.server.log.annocation.WriteLog;

@RestController
@RequestMapping(value = "/api/v1/load")
@Slf4j
public class LoadController {
	
	@Autowired
	private LoadService loadService;
	@Autowired
	private MediaServer mediaServer;
	/**
	 * @category 查询最后一圈n秒内的数据
	 * @param (loadInfo),startTime,endTime
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsg")  
	@ResponseBody
	public ResponseEntity<?> selectLoadMsg(@RequestBody LoadInfo loadInfo){

	    Integer number=loadInfo.getNumber()*4;
	    //初始化几秒的数据
	    List<LoadModel> loadModelList = loadService.selectLoadMsg(number);
	    
	    for (LoadModel loadModel : loadModelList) {
			String fullLink = mediaServer.getImageHost() + loadModel.getPicPath();
			loadModel.setPicPath(fullLink);
		  }

		    LoadInfo putLoadInfo = new LoadInfo();
	        putLoadInfo.setLoadModelList(loadModelList);
	        //获取初始化开始和结束时间
		    String startTime = loadModelList.get(0).getTime();
		    putLoadInfo.setStartTime(startTime);
		    Integer listSize = loadModelList.size();
		    String endTime = loadModelList.get(listSize-2).getTime();
		    putLoadInfo.setEndTime(endTime);

		return new ResponseEntity.Builder<LoadInfo>()
				.setData(putLoadInfo)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	
	/**
	 * @category 根据时间段去查询载体数据
	 * @param  startTime endTime
	 * @return 
	 * @throws ParseException 
	 */
	@PostMapping(value = "/selectLoadMsgByPeriod")
	@ResponseBody
	@WriteLog(value="selectLoadMsgByPeriod")
	public ResponseEntity<?> selectLoadMsgByPeriod(@RequestBody LoadInfo loadInfo) throws ParseException{


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

		List<LoadModel> loadModelList = loadService.selectLoadMsgByPeriod(startTime,endTime);
		for (LoadModel loadModel : loadModelList) {
			String fullLink = mediaServer.getImageHost() + loadModel.getPicPath();
			loadModel.setPicPath(fullLink);
		}
		return new ResponseEntity.Builder<List<LoadModel>>()
				.setData(loadModelList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}

}

