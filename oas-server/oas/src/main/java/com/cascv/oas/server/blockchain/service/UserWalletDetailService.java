package com.cascv.oas.server.blockchain.service;
import java.util.List;
import com.cascv.oas.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Ming Yang
 * Date:2018-10-09
 */
import org.springframework.stereotype.Service;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.timezone.mapper.CountryPromaryModelMapper;
import com.cascv.oas.server.timezone.model.CountryPromaryModel;
import com.cascv.oas.server.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserWalletDetailService {
	@Autowired 
	private UserWalletDetailMapper userWalletDetailMapper;
	@Autowired 
	private CountryPromaryModelMapper countryPromaryModelMapper;
	
	public List<UserWalletDetail> selectByInOrOut(String userUuid,Integer offset,Integer limit,Integer inOrOut){
		List<UserWalletDetail> userWalletDetailList = userWalletDetailMapper.selectByInOrOut(userUuid,offset,limit, inOrOut);
		String srcFormater = null,dstFormater = null;
		String dstTimeZoneId=null;
		String name=ShiroUtils.getAddress();
		log.info("name={}",name);
		if(name!=null) 
		{
			String [] arr = name.split("\\s+");
			String newName=arr[0];
			log.info("newName={}",newName);
			CountryPromaryModel countryPromaryModel=countryPromaryModelMapper.selectTimeZoneByPromaryName(newName);
			if(countryPromaryModel!=null) {
				dstTimeZoneId=countryPromaryModelMapper.selectTimeZoneByPromaryName(newName).getTimeZone();
				log.info("dstTimeZoneId={}",dstTimeZoneId);
			}else {
				dstTimeZoneId=countryPromaryModelMapper.selectTimeZoneByCountryName(newName).getTimeZone();
				log.info("dstTimeZoneId={}",dstTimeZoneId);
			}
		}else
		{
			dstTimeZoneId="Asia/Shanghai";
			log.info("dstTimeZoneId={}",dstTimeZoneId);
		}
			for(UserWalletDetail userWalletDetail : userWalletDetailList)
			{
				String created=DateUtils.string2Timezone(srcFormater, userWalletDetail.getCreated(), dstFormater, dstTimeZoneId);
				userWalletDetail.setCreated(created);
				log.info("newCreated={}",created);
			}
		
		return userWalletDetailList;
	}
	public List<UserWalletDetail> selectByPage(String userUuid,Integer offset,Integer limit){
		List<UserWalletDetail> userWalletDetailList = userWalletDetailMapper.selectByPage(userUuid, offset,limit);
		String srcFormater = null,dstFormater = null;
		String dstTimeZoneId=null;
		String name=ShiroUtils.getAddress();
		log.info("name={}",name);
		if(name!=null) 
		{
			String [] arr = name.split("\\s+");
			String newName=arr[0];
			log.info("newName={}",newName);
			CountryPromaryModel countryPromaryModel=countryPromaryModelMapper.selectTimeZoneByPromaryName(newName);
			if(countryPromaryModel!=null) {
				dstTimeZoneId=countryPromaryModelMapper.selectTimeZoneByPromaryName(newName).getTimeZone();
				log.info("dstTimeZoneId={}",dstTimeZoneId);
			}else {
				dstTimeZoneId=countryPromaryModelMapper.selectTimeZoneByCountryName(newName).getTimeZone();
				log.info("dstTimeZoneId={}",dstTimeZoneId);
			}
		}else
		{
			dstTimeZoneId="Asia/Shanghai";
			log.info("dstTimeZoneId={}",dstTimeZoneId);
		}
			for(UserWalletDetail userWalletDetail : userWalletDetailList)
			{
				String created=DateUtils.string2Timezone(srcFormater, userWalletDetail.getCreated(), dstFormater, dstTimeZoneId);
				userWalletDetail.setCreated(created);
				log.info("newCreated={}",created);
			}
		
		return userWalletDetailList;
	}
}
