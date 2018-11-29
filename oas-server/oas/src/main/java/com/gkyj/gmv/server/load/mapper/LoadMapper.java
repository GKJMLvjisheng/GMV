package com.gkyj.gmv.server.load.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.gkyj.gmv.server.load.model.LoadModel;


@Component
public interface LoadMapper {
    
//    List<LoadModel> selectLoadMsgForExcel(@Param("searchValue") String searchValue,
//			@Param("offset") Integer offset, @Param("limit") Integer limit);
    List<LoadModel> selectLoadMsg(@Param("searchValue") String searchValue);
    
    List<LoadModel> selectLoadMsgForCurve();
    
    List<LoadModel> selectLoadMsgByTime(@Param("time") String time, @Param("searchValue") String searchValue);
    
    List<LoadModel> selectLoadMsgByPeriod(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("searchValue") String searchValue);
    
    Integer countOfSelectLoadMsgForExcel(@Param("searchValue") String searchValue);
    
    String endTimeOfSelectLoadMsgForExcel();
    
    Integer countOfSelectLoadMsgByTime(@Param("searchValue") String searchValue, @Param("time") String time);
    
    Integer countOfSelectLoadMsgByPeriod(@Param("searchValue") String searchValue, @Param("startTime") String startTime, @Param("endTime") String endTime);
    
}
