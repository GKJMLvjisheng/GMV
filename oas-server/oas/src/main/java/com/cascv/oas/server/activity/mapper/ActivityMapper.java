package com.cascv.oas.server.activity.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cascv.oas.server.activity.model.ActivityModel;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;

@Component
public interface ActivityMapper {
	
	Integer insertActivity(ActivityModel activityModel);
	List<ActivityModel> selectAllActivity();
	Integer updateActivity(ActivityModel activityModel);
	ActivityCompletionStatus selectUserActivityStatusByUserUuid(String userUuid);

}
