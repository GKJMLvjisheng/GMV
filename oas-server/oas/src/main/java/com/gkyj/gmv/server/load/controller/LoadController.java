package com.gkyj.gmv.server.load.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.gkyj.gmv.server.load.mapper.LoadMapper;
import com.gkyj.gmv.server.load.model.LoadModel;
import com.gkyj.gmv.server.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import com.gkyj.gmv.server.log.annocation.WriteLog;

@RestController
@RequestMapping(value = "/api/v1/miner")
@Slf4j
public class LoadController {
	
	@Autowired
	private LoadMapper loadMapper;
	
	/**
	 * @category 查询所有数据，包括开始时间和结束时间
	 * @param (searchValue, pageSize, pageNum)
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsg")  
	@ResponseBody
	public ResponseEntity<?> selectLoadMsg(@RequestBody PageDomain<Integer> pageInfo){
		
		Integer pageNum = pageInfo.getPageNum();
	    Integer pageSize = pageInfo.getPageSize();
	    Integer limit = pageSize;
	    Integer offset;
	    if (pageSize ==null) {
	      limit = 10;
	    }
	    if (pageNum != null && pageNum > 0)
	    	offset = (pageNum - 1) * limit;
	    else 
	    	offset = 0;
	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	    	     
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsg(searchValue, offset, limit);	    
	   
	    String startTime = loadModelList.get(0).getUpdated();
		String endTime = loadModelList.get(loadModelList.size()-1).getUpdated();
	    
	    PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
	    Integer count = loadModelList.size();
	    	
	    loadRecordPage.setTotal(count);
	    loadRecordPage.setAsc("asc");
	    loadRecordPage.setOffset(offset);
	    loadRecordPage.setPageNum(pageNum);
	    loadRecordPage.setPageSize(pageSize);
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
	 * @param  updated,(searchValue, pageSize, pageNum)
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsgByTime")  
	@ResponseBody
	public ResponseEntity<?> selectLoadMsgByTime(@RequestBody PageDomain<Integer> pageInfo){
		
		Integer pageNum = pageInfo.getPageNum();
	    Integer pageSize = pageInfo.getPageSize();
	    Integer limit = pageSize;
	    Integer offset;
	    
	    //String time = pageInfo.getPageNum();
	    
	    if (pageSize ==null) {
	      limit = 10;
	    }
	    if (pageNum != null && pageNum > 0)
	    	offset = (pageNum - 1) * limit;
	    else 
	    	offset = 0;
	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	    	     
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsgByTime(pageInfo.getTime(),searchValue, offset, limit);	    
	    
	    PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
	    Integer count = loadModelList.size();
	    	
	    loadRecordPage.setTotal(count);
	    loadRecordPage.setOffset(offset);
	    loadRecordPage.setPageNum(pageNum);
	    loadRecordPage.setPageSize(pageSize);
	   
	    loadRecordPage.setRows(loadModelList);
	   
		return new ResponseEntity.Builder<PageDomain<LoadModel>>()
				.setData(loadRecordPage)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	/**
	 * @category 根据时间段去查询载体数据
	 * @param  startTime, endTime(searchValue, pageSize, pageNum)
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsgByPeriod")  
	@ResponseBody
	@WriteLog(value="selectLoadMsgByPeriod")
	public ResponseEntity<?> selectLoadMsgByPeriod(@RequestBody PageDomain<Integer> pageInfo){
		
		Integer pageNum = pageInfo.getPageNum();
	    Integer pageSize = pageInfo.getPageSize();
	    Integer limit = pageSize;
	    Integer offset;
	    
	    String startTime = pageInfo.getStartTime();
	    String endTime = pageInfo.getEndTime();	    
	    
//	    String updated1 = pageInfo.getUpdated();	    
//	    int i = Integer.valueOf(updated1).intValue();	    
//	    String updated2 = Integer.toString(i+4);	    
//	    log.info("updated1={}",updated1);
//	    log.info("updated2={}",updated2);
	    
	    if (pageSize ==null) {
	      limit = 10;
	    }
	    if (pageNum != null && pageNum > 0)
	    	offset = (pageNum - 1) * limit;
	    else 
	    	offset = 0;
	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	    	     
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsgByPeriod(startTime,endTime,searchValue, offset, limit);	    
	    
	    PageDomain<LoadModel> loadRecordPage = new PageDomain<>();
	    Integer count = loadModelList.size();
	    	
	    loadRecordPage.setTotal(count);
	    loadRecordPage.setOffset(offset);
	    loadRecordPage.setPageNum(pageNum);
	    loadRecordPage.setPageSize(pageSize);
	   
	    loadRecordPage.setRows(loadModelList);
	   
		return new ResponseEntity.Builder<PageDomain<LoadModel>>()
				.setData(loadRecordPage)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	
	/**
	 * @category 查询所有数据，包括开始时间和结束时间
	 * @param 无
	 * @return 
	 */
	@PostMapping(value = "/selectLoadMsgForCurve")  
	@ResponseBody
	public ResponseEntity<?> selectLoadMsgForCurve(@RequestBody PageDomain<Integer> pageInfo){
	    	     
	    List<LoadModel> loadModelList = loadMapper.selectLoadMsgForCurve();	    
	   
	    String startTime = loadModelList.get(0).getUpdated();
		String endTime = loadModelList.get(loadModelList.size()-1).getUpdated();
	    
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
}

