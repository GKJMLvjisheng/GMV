package com.gkyj.gmv.server.load.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.core.common.PageDomain;
import com.gkyj.gmv.server.load.config.MediaServer;
import com.gkyj.gmv.server.load.mapper.LoadMapper;
import com.gkyj.gmv.server.load.model.LoadModel;
import com.gkyj.gmv.server.utils.ShiroUtils;
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
	 * @category 查询最后一圈开始时刻的数据
	 * @param (searchValue)
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsg")  
	@ResponseBody
	public ResponseEntity<?> selectLoadMsg(@RequestBody PageDomain<Integer> pageInfo){
		
		/*Integer pageNum = pageInfo.getPageNum();
	    Integer pageSize = pageInfo.getPageSize();
	    Integer limit = pageSize;
	    Integer offset;
	    if (pageSize ==null) {
	      limit = 10;
	    }
	    if (pageNum != null && pageNum > 0)
	    	offset = (pageNum - 1) * limit;
	    else 
	    	offset = 0;*/
	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	    	     
	    //List<LoadModel> loadModelList = loadMapper.selectLoadMsgForExcel(searchValue, offset, limit);
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsg(searchValue);
	    
	    for (LoadModel loadModel : loadModelList) {
			String fullLink = mediaServer.getImageHost() + loadModel.getLoadPicturePath();
			loadModel.setLoadPicturePath(fullLink);
		  }
	   
	    String startTime = loadModelList.get(0).getUpdated();
		String endTime = loadMapper.endTimeOfSelectLoadMsgForExcel();
	    
	    PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
	    Integer count = loadMapper.countOfSelectLoadMsgForExcel(searchValue);
	    loadRecordPage.setTotal(count);
	    loadRecordPage.setAsc("asc");
//	    loadRecordPage.setOffset(offset);
//	    loadRecordPage.setPageNum(pageNum);
//	    loadRecordPage.setPageSize(pageSize);
	    loadRecordPage.setStartTime(startTime);
	    loadRecordPage.setEndTime(endTime);
	    loadRecordPage.setRows(loadModelList);
	   
		return new ResponseEntity.Builder<PageDomain<LoadModel>>()
				.setData(loadRecordPage)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	/**
	 * @category 查询所有数据，返回开始时间和结束时间
	 * @param 无
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsgForCurve")  
	@ResponseBody
	public ResponseEntity<?> selectLoadMsgForCurve(@RequestBody PageDomain<Integer> pageInfo){
	    	     
	    List<LoadModel> loadModelList1 = loadMapper.selectLoadMsgForCurve();	 
	    
	    List<LoadModel> loadModelList = loadModelList1.subList(0, 10);
	    
	    String startTime = loadModelList1.get(0).getUpdated();
		String endTime = loadModelList1.get(loadModelList1.size()-1).getUpdated();
	    
	    PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
	    Integer count = loadModelList.size();
	    	
	    loadRecordPage.setTotal(count);
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
	 * @category 根据给定时间去查询载体数据
	 * @param  updated,(searchValue)
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsgByTime")  
	@ResponseBody
	public ResponseEntity<?> selectLoadMsgByTime(@RequestBody PageDomain<Integer> pageInfo){
		
		/*Integer pageNum = pageInfo.getPageNum();
	    Integer pageSize = pageInfo.getPageSize();
	    Integer limit = pageSize;
	    Integer offset;
	    
	    if (pageSize ==null) {
	      limit = 10;
	    }
	    if (pageNum != null && pageNum > 0)
	    	offset = (pageNum - 1) * limit;
	    else 
	    	offset = 0;*/
		String time = pageInfo.getTime();
	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	    	     
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsgByTime(time,searchValue);
	    for (LoadModel loadModel : loadModelList) {
			String fullLink = mediaServer.getImageHost() + loadModel.getLoadPicturePath();
			loadModel.setLoadPicturePath(fullLink);
		  }
	    
	    PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
	    //Integer count = loadModelList.size();
	    Integer count = loadMapper.countOfSelectLoadMsgByTime(searchValue,time);	
	    loadRecordPage.setTotal(count);
//	    loadRecordPage.setOffset(offset);
//	    loadRecordPage.setPageNum(pageNum);
//	    loadRecordPage.setPageSize(pageSize);
	    loadRecordPage.setTime(time);
	   
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
		
		/*Integer pageNum = pageInfo.getPageNum();
	    Integer pageSize = pageInfo.getPageSize();
	    Integer limit = pageSize;
	    Integer offset;
	    
	     if (pageSize ==null) {
	      limit = 10;
	    }
	    if (pageNum != null && pageNum > 0)
	    	offset = (pageNum - 1) * limit;
	    else 
	    	offset = 0;*/
		
		String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
		
	    String startTime = pageInfo.getStartTime();
	    String endTime3 = pageInfo.getEndTime();	 
	    Date endTime1 = null;
	    String endTime = null;
		
	    //开始时间加3秒--字符串转Date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		  
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		endTime1 = format1.parse(startTime);  		  		 
		log.info("当前时间={}" ,sdf.format(endTime1));
		Date afterTime = new Date(endTime1 .getTime() + 2000);
		log.info("处理后时间={}" ,sdf.format(afterTime));
		//Date转字符串		
		String endTime2 = sdf.format(afterTime);  
		
		if(endTime3==null) {
			endTime = endTime2; //前端输入开始时间加3秒=结束时间
		}else {
			endTime = endTime3; //前端输入的结束时间   
		}
  
	    	     
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsgByPeriod(startTime,endTime,searchValue);
	    for (LoadModel loadModel : loadModelList) {
			String fullLink = mediaServer.getImageHost() + loadModel.getLoadPicturePath();
			loadModel.setLoadPicturePath(fullLink);
		  }
	    
	    PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
	    //Integer count = loadModelList.size();
	    Integer count = loadMapper.countOfSelectLoadMsgByPeriod(searchValue,startTime,endTime);	
	    loadRecordPage.setTotal(count);
//	    loadRecordPage.setOffset(offset);
//	    loadRecordPage.setPageNum(pageNum);
//	    loadRecordPage.setPageSize(pageSize);
	    loadRecordPage.setStartTime(startTime);
	    loadRecordPage.setEndTime(endTime);
	    loadRecordPage.setRows(loadModelList);
	   
		return new ResponseEntity.Builder<PageDomain<LoadModel>>()
				.setData(loadRecordPage)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
}

