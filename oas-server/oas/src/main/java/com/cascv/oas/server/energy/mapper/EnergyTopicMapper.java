package com.cascv.oas.server.energy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.cascv.oas.server.energy.model.EnergyTopicModel;





@Component
public interface EnergyTopicMapper {
	 Integer insertTopic(EnergyTopicModel energyTopicModel);
	 Integer deleteTopic(Integer topicId);
	 Integer updateTopic(EnergyTopicModel energyTopicModel);
	 List<EnergyTopicModel> selectAllTopic();	 
	 List<EnergyTopicModel> selectPage(@Param("offset") Integer offset, @Param("limit") Integer limit); 
     Integer countTotal();
}
