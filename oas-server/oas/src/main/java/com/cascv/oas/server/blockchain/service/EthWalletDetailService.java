package com.cascv.oas.server.blockchain.service;
import java.util.List;
import com.cascv.oas.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Ming Yang
 * Date:2018-10-09
 */
import org.springframework.stereotype.Service;

import com.cascv.oas.server.blockchain.mapper.EthWalletDetailMapper;
import com.cascv.oas.server.blockchain.model.EthWalletDetail;
import com.cascv.oas.server.timezone.mapper.CountryPromaryModelMapper;
import com.cascv.oas.server.timezone.model.CountryPromaryModel;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EthWalletDetailService {
	@Autowired 
	private EthWalletDetailMapper ethWalletDetailMapper;
	@Autowired 
	private TimeZoneService timeZoneService;
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
}
