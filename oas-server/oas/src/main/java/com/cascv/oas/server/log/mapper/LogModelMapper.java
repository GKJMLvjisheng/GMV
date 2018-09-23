package com.cascv.oas.server.log.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.log.model.LogModel;

public interface LogModelMapper {
  Integer insertSelective(LogModel logModel);
  List<LogModel> selectPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
  Integer countTotal();
}
