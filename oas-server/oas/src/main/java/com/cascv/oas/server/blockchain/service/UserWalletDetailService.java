package com.cascv.oas.server.blockchain.service;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Ming Yang
 * Date:2018-10-09
 */
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.PageDomainObject;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.SystemResq;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.timezone.service.TimeZoneService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserWalletDetailService {
	@Autowired 
	private UserWalletDetailMapper userWalletDetailMapper;
	@Autowired
	private UserWalletMapper userWalletMapper;
	@Autowired 
	private TimeZoneService timeZoneService;
	
	public List<UserWalletDetail> selectByInOrOut(String userUuid,Integer offset,Integer limit,Integer inOrOut){
		List<UserWalletDetail> userWalletDetailList = userWalletDetailMapper.selectByInOrOut(userUuid,offset,limit, inOrOut);

			for(UserWalletDetail userWalletDetail : userWalletDetailList)
			{
				String srcFormater="yyyy-MM-dd HH:mm:ss";
				String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, userWalletDetail.getCreated(), dstFormater, dstTimeZoneId);
				userWalletDetail.setCreated(created);
				log.info("newCreated={}",created);
			}
		
		return userWalletDetailList;
	}
	public List<UserWalletDetail> selectByPage(String userUuid,Integer offset,Integer limit){
		List<UserWalletDetail> userWalletDetailList = userWalletDetailMapper.selectByPage(userUuid, offset,limit);
		
			for(UserWalletDetail userWalletDetail : userWalletDetailList)
			{
				String srcFormater="yyyy-MM-dd HH:mm:ss";
				String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, userWalletDetail.getCreated(), dstFormater, dstTimeZoneId);
				userWalletDetail.setCreated(created);
				log.info("newCreated={}",created);
			}
		
		return userWalletDetailList;
	}
	
	 public PageDomainObject<SystemResq<UserWalletDetail>> systemTransactionDetail(Integer pageNum,Integer pageSize) {
		  PageDomainObject<SystemResq<UserWalletDetail>> result = new PageDomainObject<SystemResq<UserWalletDetail>>();
		  result.setPageNum(pageNum);
		  result.setPageSize(pageSize);
		  result.setOffset((pageNum - 1)*pageSize);
		  List<UserWalletDetail> list = userWalletDetailMapper.getSystemDetailByPage((pageNum - 1)*pageSize, pageSize);
		  if(list!=null) {
			  for(UserWalletDetail ud:list) {
				  ud.setCreated(getTimeAfterExchange(ud.getCreated()));
			  }
		  }
		  SystemResq<UserWalletDetail> resq = new SystemResq<UserWalletDetail>();
		  resq.setList(list);
		  UserWallet systemWallet = userWalletMapper.getSystemWallet();
		  resq.setValue(systemWallet == null?BigDecimal.ZERO:systemWallet.getBalance());
		  result.setRows(resq);
		  result.setTotal(userWalletDetailMapper.getSystemDetailCount());
		  return result;
	  }
	 
	 public String getTimeAfterExchange(String beforeCreated) {
		String srcFormater="yyyy-MM-dd HH:mm:ss";
	    String dstFormater="yyyy-MM-dd HH:mm:ss";
	    String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
		String created=DateUtils.string2Timezone(srcFormater, beforeCreated , dstFormater, dstTimeZoneId);
		return created;
	}
		  
}
