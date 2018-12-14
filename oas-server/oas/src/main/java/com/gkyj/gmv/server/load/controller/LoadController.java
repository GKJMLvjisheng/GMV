package com.gkyj.gmv.server.load.controller;

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
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.gkyj.gmv.server.load.config.MediaServer;
import com.gkyj.gmv.server.load.mapper.LoadMapper;
import com.gkyj.gmv.server.load.model.LoadModel;
import lombok.extern.slf4j.Slf4j;
import com.gkyj.gmv.server.log.annocation.WriteLog;

@RestController
@RequestMapping(value = "/api/v1/load")
@Slf4j
public class LoadController {
	
	@Autowired
	private LoadMapper loadMapper;
	@Autowired
	private MediaServer mediaServer;
	/**
	 * @category 查询最后一圈n秒内的数据
	 * @param (searchValue)
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsg")  
	@ResponseBody
	public ResponseEntity<?> selectLoadMsg(@RequestBody PageDomain<Integer> pageInfo){
		
	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	    Integer number=pageInfo.getNumber()-1;
	    	     
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsg(number, searchValue);
	    
	    for (LoadModel loadModel : loadModelList) {
			String fullLink = mediaServer.getImageHost() + loadModel.getLoadPicturePath();
			loadModel.setLoadPicturePath(fullLink);
		  }
	   
	    String startTime = loadModelList.get(0).getUpdated();
		String endTime = loadMapper.endTimeOfSelectLoadMsg();
	    
	    PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
	    Integer count = loadMapper.countOfSelectLoadMsg(number, searchValue);
	    Integer count1 = loadMapper.countOfSelectLoadMsgLastCircle();
	    loadRecordPage.setTotal(count);
	    loadRecordPage.setTotalOfLast(count1);
	    loadRecordPage.setAsc("asc");
	    loadRecordPage.setStartTime(startTime);
	    loadRecordPage.setEndTime(endTime);
	    loadRecordPage.setRows(loadModelList);
	   
		return new ResponseEntity.Builder<PageDomain<LoadModel>>()
				.setData(loadRecordPage)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	
	/**
	 * @category 根据时间段去查询载体数据
	 * @param  startTime(endTime, searchValue)
	 * @return 
	 * @throws ParseException 
	 */
	@PostMapping(value = "/selectLoadMsgByPeriod")  
	@ResponseBody
	@WriteLog(value="selectLoadMsgByPeriod")
	public ResponseEntity<?> selectLoadMsgByPeriod(@RequestBody PageDomain<Integer> pageInfo) throws ParseException{
		
		String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
		
	    String startTime = pageInfo.getStartTime();
	    String endTime3 = pageInfo.getEndTime();
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
  
		PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
		
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsgByPeriod(startTime,endTime,searchValue);
	    for (LoadModel loadModel : loadModelList) {
			String fullLink = mediaServer.getImageHost() + loadModel.getLoadPicturePath();
			loadModel.setLoadPicturePath(fullLink);
		  }
	    
	    if(loadModelList.size()==0) {
	    	loadRecordPage.setMsg("数据库中无匹配的数据！");
	    }
	    
	    Integer count = loadMapper.countOfSelectLoadMsgByPeriod(searchValue,startTime,endTime);	
	    loadRecordPage.setTotal(count);
	    loadRecordPage.setStartTime(startTime);
	    loadRecordPage.setEndTime(endTime);
	    loadRecordPage.setRows(loadModelList);
	   
		return new ResponseEntity.Builder<PageDomain<LoadModel>>()
				.setData(loadRecordPage)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
}

