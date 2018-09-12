package com.cascv.oas.server.blockchain.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;


public interface UserWalletDetailMapper {
  Integer insertSelective(UserWalletDetail userWalletDetail);
  List<UserWalletDetail> selectByUserUuid(String useruuid);
  
  Integer selectCount(@Param("userUuid") String userUuid);
  List<UserWalletDetail> selectByPage(@Param("userUuid") String userUuid, @Param("offset") Integer offset, @Param("limit") Integer limit);
  Integer deleteByUserUuid(String userUuid);
  Integer deleteByUuid(String uuid);
  
  List<UserWalletDetail> selectByInOrOut(@Param("userUuid") String userUuid,@Param("offset") Integer offset,@Param("limit") Integer limit, @Param("inOrOut") Integer inOrOut);
}
