package com.cascv.oas.server.blockchain.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.EthWalletDetail;
import com.cascv.oas.server.user.model.UserModel;

public interface EthWalletDetailMapper {
  Integer insertSelective(EthWalletDetail ethWalletDetail);
  Integer update(EthWalletDetail ethWalletDetail);
  List<EthWalletDetail> selectByAddress(String address);  
  Integer selectCount(@Param("address") String address);
  List<EthWalletDetail> selectByPage(@Param("address") String address, @Param("offset") Integer offset, @Param("limit") Integer limit);
  Integer deleteByAddress(String address);
  Integer deleteByUuid(String uuid);
  List<EthWalletDetail> selectByInOrOut(@Param("address") String address, @Param("offset") Integer offset, @Param("limit") Integer limit,@Param("inOrOut") Integer inOrOut);

  Integer updateByPrimaryKeySelective(EthWalletDetail ethWalletDetail);
  EthWalletDetail selectByUUid(String uuid);
  UserModel selectUserByAddress(String address);

  List<EthWalletDetail> selectEthTransactionJob(@Param("network") String network, @Param("limit") Integer limit); 
  
  /**
   * 根据hash值返回记录
   * @param hash
   * @return
   */
  List<EthWalletDetail> getEthRecordByHash(String hash);
  /**
   * 根据hash值更新restbalance
   * @param hash
   * @param restBalance
   * @return
   */
  Integer updateRestBalanceByHash(@Param("uuid") String uuid,@Param("restBalance") BigDecimal restBalance);
  
  /**
   * 获取system的交易记录分页
   * @return
   */
  List<EthWalletDetail> getSystemDetailByPage(@Param("offset") Integer offset,@Param("limit") Integer limit);
  
  /**
   * 获取system记录总数
   * @return
   */
  Integer getSystemDetailCount();
  /**
   * 获取system的交易钱包address
   * @return
   */
  String getSystemAddress();
  /**
   * 获取hash的记录数量
   * @return
   */
  Integer getRecordCountByHash(String txHash);

}
