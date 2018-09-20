package com.cascv.oas.server.energy.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.cascv.oas.server.energy.model.EnergyChoice;
import com.cascv.oas.server.energy.model.EnergyQuestion;



@Component
public interface EnergyTopicMapper {
	 Integer insertQuestions(EnergyQuestion energyQuestion);

	 Integer insertAnswers(List<EnergyChoice> answers);
	 
//	 Integer updateNews(NewsModel newsModel);
//	 
//	 List<NewsModel> selectAllNews();	 
//	 
//	 List<NewsModel> selectPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
	 
     Integer countTotal();
}
