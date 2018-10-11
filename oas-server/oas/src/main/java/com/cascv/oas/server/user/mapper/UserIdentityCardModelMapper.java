package com.cascv.oas.server.user.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * @author Ming Yang
 */
import org.springframework.stereotype.Component;
import com.cascv.oas.server.user.model.UserIdentityCardModel;

@Component
public interface UserIdentityCardModelMapper {

	Integer updateUserIdentityCardByNameNumberRemarkVerifyStatus(UserIdentityCardModel userIdentityCardModel);
	
	Integer updateUserIdentityCardByFrontOfPhoto(UserIdentityCardModel userIdentityCardModel);
	
	Integer updateUserIdentityCardByBackOfPhoto(UserIdentityCardModel userIdentityCardModel);
	
	Integer updateUserIdentityCardByHoldInHand(UserIdentityCardModel userIdentityCardModel);
	
	List<UserIdentityCardModel> selectAllUserIdentityCard();
	
	UserIdentityCardModel selectUserIdentityByUserName(@Param("userName") String userName);
	
}
