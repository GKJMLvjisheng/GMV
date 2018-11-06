package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.OasDetail;
import com.cascv.oas.server.blockchain.model.OasDetailResp;
import com.cascv.oas.server.blockchain.model.OasReq;
import com.cascv.oas.server.user.model.UserModel;

public interface OasDetailMapper {
  /**
   * 插入提币充币记录
   * @param detail
   * @return
   */
  Integer insertSelective(OasDetail detail);
  /**
   * 获取提币记录，用于返回前台显示
   * @return
   */
  List<OasDetailResp> getAllWithdrawRecord(@Param("offset") Integer offset,@Param("limit") Integer limit);
  /**
   * 返回提币总条数
   * @return
   */
  Integer getAllWithdrawRecordCount();
  /**
   * 获取提币手续费
   * @return
   */
  String getOasExtra(String code);
  /**
   * 更新提币手续费
   * @param value
   * @return
   */
  Integer updateOasExtra(@Param("detail") OasReq oasDetail,@Param("updated") String updated);
  /**
   * 根据uuid查询提币充币记录
   * @param uuid
   * @return
   */
  OasDetail getRecordByUuid(@Param("uuid")String uuid);
  /**
   * 根据uuid更新提币事件状态
   * @param uuid
   * @param result
   * @return
   */
  Integer setWithdrawResultByUuid(@Param("uuid")String uuid,@Param("result") Integer result,@Param("updated")String updated,@Param("txHash") String hash);
  /**
   * 查询出system用户信息
   * @return
   */
  UserModel getSystemUserInfo();
  /**
   * 查询出admin用户信息
   * @return
   */
  UserModel getAdminUserInfo();
  /**
   * 获取firstone用户信息
   * @return
   */
  UserModel getFirstOneUserInfo();
  /**
   * 根据hash值更新记录
   * @param hash
   * @return
   */
  Integer updateRecordByHash(OasDetail detail);
  /**
   * 根据hash值查找记录
   * @param txHash
   * @return
   */
  OasDetail selectRecordByHash(String txHash);
  /**
   * 根据uuid更新状态
   * @param uuid
   * @return
   */
  Integer updateStatusByUuid(@Param("uuid")String uuid,@Param("status")Integer status);
}
