package com.cascv.oas.server.reward.mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @author Ming Yang
 */
import org.springframework.stereotype.Component;

import com.cascv.oas.server.reward.model.PromotedRewardModel;

import java.util.List;

@Component
public interface PromotedRewardModelMapper {
	
	List<PromotedRewardModel> selectAllPromotedRewards();
	
	PromotedRewardModel selectPromotedRewardByRewardName(@Param("rewardName") String rewardName);
	
	Integer updatePromotedReward(PromotedRewardModel promotedRewardModel);
	
}