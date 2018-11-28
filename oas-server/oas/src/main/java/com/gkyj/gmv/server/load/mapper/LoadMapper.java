package com.gkyj.gmv.server.load.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.gkyj.gmv.server.load.model.LoadModel;


@Component
public interface LoadMapper {
    
    List<LoadModel> selectLoadMsg(@Param("searchValue") String searchValue,
			@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    List<LoadModel> selectLoadMsgForCurve();
    
    List<LoadModel> selectLoadMsgByTime(@Param("updated") String updated,@Param("searchValue") String searchValue,
			@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    List<LoadModel> selectLoadMsgByPeriod(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("searchValue") String searchValue,
			@Param("offset") Integer offset, @Param("limit") Integer limit);
    
}
