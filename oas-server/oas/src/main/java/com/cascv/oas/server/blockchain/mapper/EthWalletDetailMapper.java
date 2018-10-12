package com.cascv.oas.server.blockchain.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.EthWalletDetail;

public interface EthWalletDetailMapper {
  Integer insertSelective(EthWalletDetail ethWalletDetail);
  Integer update(EthWalletDetail ethWalletDetail);
  List<EthWalletDetail> selectByAddress(String address);  
  Integer selectCount(@Param("address") String address);
  List<EthWalletDetail> selectByPage(@Param("address") String address, @Param("offset") Integer offset, @Param("limit") Integer limit);
  Integer deleteByAddress(String address);
  Integer deleteByUuid(String uuid);
  List<EthWalletDetail> selectByInOrOut(@Param("address") String address, @Param("offset") Integer offset, @Param("limit") Integer limit,@Param("inOrOut") Integer inOrOut);
  List<EthWalletDetail> selectEthTransactionJob(@Param("network") String network, @Param("limit") Integer limit); 
}
