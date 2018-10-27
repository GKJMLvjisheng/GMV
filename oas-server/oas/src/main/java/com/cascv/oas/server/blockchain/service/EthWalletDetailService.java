package com.cascv.oas.server.blockchain.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Ming Yang
 * Date:2018-10-09
 */
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.PageDomainObject;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.mapper.EthWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.OasDetailMapper;
import com.cascv.oas.server.blockchain.model.EthWalletDetail;
import com.cascv.oas.server.blockchain.model.SystemResq;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.user.model.UserModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EthWalletDetailService {
	@Autowired 
	private EthWalletDetailMapper ethWalletDetailMapper;
	@Autowired 
	private TimeZoneService timeZoneService;
	@Autowired 
	private EthWalletService ethWalletService;
	@Autowired
	private OasDetailMapper oasDetailMapper;
	
	public List<EthWalletDetail> selectByInOrOut(String userUuid,Integer offset,Integer limit,Integer inOrOut){
		List<EthWalletDetail> ethWalletDetailList = ethWalletDetailMapper.selectByInOrOut(userUuid,offset,limit, inOrOut);
		    
			for(EthWalletDetail ethWalletDetail : ethWalletDetailList)
			{
				String srcFormater="yyyy-MM-dd HH:mm:ss";
			    String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, ethWalletDetail.getCreated(), dstFormater, dstTimeZoneId);
				ethWalletDetail.setCreated(created);
				log.info("newCreated={}",created);
			}
		
		return ethWalletDetailList;
	}
	public List<EthWalletDetail> selectByPage(String userUuid,Integer offset,Integer limit){
		List<EthWalletDetail> ethWalletDetailList = ethWalletDetailMapper.selectByPage(userUuid, offset,limit);
	
			for(EthWalletDetail ethWalletDetail : ethWalletDetailList)
			{
				String srcFormater="yyyy-MM-dd HH:mm:ss";
			    String dstFormater="yyyy-MM-dd HH:mm:ss";
			    String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, ethWalletDetail.getCreated(), dstFormater, dstTimeZoneId);
				ethWalletDetail.setCreated(created);
				log.info("newCreated={}",created);
			}
		
		return ethWalletDetailList;
	}
	
	 public PageDomainObject<SystemResq<EthWalletDetail>> systemTransactionDetail(Integer pageNum,Integer pageSize) {
		 PageDomainObject<SystemResq<EthWalletDetail>> result = new PageDomainObject<SystemResq<EthWalletDetail>>();
		  result.setPageNum(pageNum);
		  result.setPageSize(pageSize);
		  result.setOffset((pageNum - 1)*pageSize);
		  List<EthWalletDetail> list = ethWalletDetailMapper.getSystemDetailByPage((pageNum - 1)*pageSize, pageSize);
		  if(list!=null) {
			  for(EthWalletDetail ed:list) {
				  ed.setCreated(getTimeAfterExchange(ed.getCreated()));
			  }
		  }
		  SystemResq<EthWalletDetail> resq = new SystemResq<EthWalletDetail>();
		  resq.setList(list);
		  UserModel systemUser = oasDetailMapper.getSystemUserInfo();
		  if(systemUser!=null) {
			  UserCoin tokenCoin = ethWalletService.getUserCoin(systemUser.getUuid());
			  if(tokenCoin!=null) {
				  resq.setValue(tokenCoin.getBalance());
			  }
		  }
		  result.setRows(resq);
		  result.setTotal(ethWalletDetailMapper.getSystemDetailCount());
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
