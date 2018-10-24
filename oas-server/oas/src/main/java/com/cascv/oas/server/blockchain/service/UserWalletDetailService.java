package com.cascv.oas.server.blockchain.service;
import java.util.List;

import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Ming Yang
 * Date:2018-10-09
 */
import org.springframework.stereotype.Service;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserWalletDetailService {
	@Autowired 
	private UserWalletDetailMapper userWalletDetailMapper;
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
	
	 public PageDomain<UserWalletDetail> systemTransactionDetail(Integer pageNum,Integer pageSize) {
		  PageDomain<UserWalletDetail> result = new PageDomain<UserWalletDetail>();
		  result.setPageNum(pageNum);
		  result.setPageSize(pageSize);
		  result.setOffset((pageNum - 1)*pageSize);
		  List<UserWalletDetail> list = userWalletDetailMapper.getSystemDetailByPage((pageNum - 1)*pageSize, pageSize);
		  if(list!=null) {
			  for(UserWalletDetail ud:list) {
				  ud.setCreated(getTimeAfterExchange(ud.getCreated()));
			  }
		  }
		  result.setRows(list);
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
