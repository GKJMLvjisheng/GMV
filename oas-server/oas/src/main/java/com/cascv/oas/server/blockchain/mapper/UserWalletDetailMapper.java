package com.cascv.oas.server.blockchain.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;

public interface UserWalletDetailMapper {
  Integer insertSelective(UserWalletDetail userWalletDetail);
  List<UserWalletDetail> selectByUserUuid(String useruuid);
  List<UserWalletDetail> selectByDateRange(@Param("userUuid") String userUuid, @Param("fromDate") String fromDate, @Param("toDate")String toDate);
  Integer deleteByUserUuid(String userUuid);
  Integer deleteByUuid(String uuid);
}
