package com.cascv.oas.server.user.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * @author Ming Yang
 */
import org.springframework.stereotype.Component;
import com.cascv.oas.server.user.model.UserIdentityCardModel;
import com.cascv.oas.server.user.wrapper.UserDetailModel;

@Component
public interface UserIdentityCardModelMapper {
	UserDetailModel inquireUserKYCInfo(@Param("name") String name);
	
	Integer insertUserIdentityCard(UserIdentityCardModel userIdentityCardModel);
	
	Integer updateUserIdentityCardByNameNumberRemarkVerifyStatus(UserIdentityCardModel userIdentityCardModel);
	Integer updateUserIdentityCardByVerifyStatus(UserIdentityCardModel userIdentityCardModel);
	Integer updateUserIdentityCardByFrontOfPhoto(UserIdentityCardModel userIdentityCardModel);
	
	Integer updateUserIdentityCardByBackOfPhoto(UserIdentityCardModel userIdentityCardModel);
	
	Integer updateUserIdentityCardByHoldInHand(UserIdentityCardModel userIdentityCardModel);
	
	List<UserIdentityCardModel> selectAllUserIdentityCardBySearchValue(@Param("offset") Integer offset, @Param("limit") Integer limit,@Param("searchValue") String searchValue);
	Integer countBySearchValue(@Param("searchValue") String searchValue);
	
	List<UserIdentityCardModel> selectUserIdentityByUserName(@Param("userName") String userName);
	UserIdentityCardModel selectUserIdentityByUserNameVerifyStatus(@Param("userName") String userName);
	UserIdentityCardModel selectUserByIdentityNumber(@Param("userIdentityNumber") String userIdentityNumber);
	
}
