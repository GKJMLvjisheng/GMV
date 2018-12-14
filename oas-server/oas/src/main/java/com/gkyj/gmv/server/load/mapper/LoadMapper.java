package com.gkyj.gmv.server.load.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.gkyj.gmv.server.load.model.LoadModel;


@Component
public interface LoadMapper {
    
//    List<LoadModel> selectLoadMsgForExcel(@Param("searchValue") String searchValue,
//			@Param("offset") Integer offset, @Param("limit") Integer limit);
    List<LoadModel> selectLoadMsg(@Param("number") Integer number, @Param("searchValue") String searchValue);
    
    List<LoadModel> selectLoadMsgByPeriod(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("searchValue") String searchValue);
    
    Integer countOfSelectLoadMsg(@Param("number") Integer number, @Param("searchValue") String searchValue);
    
    String endTimeOfSelectLoadMsg();
    
    Integer countOfSelectLoadMsgByPeriod(@Param("searchValue") String searchValue, @Param("startTime") String startTime, @Param("endTime") String endTime);
    
    Integer countOfSelectLoadMsgLastCircle();
    
}
