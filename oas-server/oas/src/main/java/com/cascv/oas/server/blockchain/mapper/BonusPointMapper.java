package com.cascv.oas.server.blockchain.mapper;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.BonusPoint;

public interface BonusPointMapper {
  Integer insertSelective(BonusPoint bonusPoint);
  
  BonusPoint selectById(Integer id);
  BonusPoint selectByUserId(Integer userId);
  
  Integer increaseBalance(@Param("id")  Integer id, @Param("value")  Integer value);
  Integer decreaseBalance(@Param("id")  Integer id, @Param("value")  Integer value);
  
  Integer increasePower(@Param("id")  Integer id, @Param("value") Integer value);
  Integer decreasePower(@Param("id")  Integer id, @Param("value") Integer value);
  
  Integer deleteById(Integer id);
  Integer deleteByUserId(Integer userId);
}
