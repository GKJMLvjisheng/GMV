package com.cascv.oas.server.reward.mapper;
/**
 * @author Ming Yang
 */
import org.springframework.stereotype.Component;

import com.cascv.oas.server.reward.model.PromotedRewardModel;

import java.util.List;

@Component
public interface PromotedRewardModelMapper {
	
	List<PromotedRewardModel> selectAllPromotedRewards();
	
	Integer updatePromotedReward(PromotedRewardModel promotedRewardModel);
	
}