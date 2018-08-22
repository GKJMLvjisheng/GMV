package com.cascv.oas.server.blockchain.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;

public interface UserWalletDetailMapper {
  Integer insertSelective(UserWalletDetail userWalletDetail);
  List<UserWalletDetail> selectByUserId(Integer userId);
  List<UserWalletDetail> selectByDateRange(@Param("userId") Integer userId, @Param("fromDate") String fromDate, @Param("toDate")String toDate);
  Integer deleteByUserId(Integer userId);
  Integer deleteByUuid(Integer uuid);
}
