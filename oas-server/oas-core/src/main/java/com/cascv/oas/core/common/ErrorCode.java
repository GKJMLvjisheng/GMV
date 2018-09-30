package com.cascv.oas.core.common;

import lombok.Getter;
import lombok.Setter;

//abstract public class ErrorCode {
//  public static final int SUCCESS = 0;
//  public static final int GENERAL_ERROR = 10001;
//  public static final int USER_ALREADY_EXISTS = 10002;
//  public static final int USERNAME_NULL = 10003;
//  public static final int PASSWORD_NULL = 10004;
//}


public enum ErrorCode {
  SUCCESS(0, "成功"),
  GENERAL_ERROR(10001, "错误"),
  USER_ALREADY_EXISTS(10002, "用户已存在"),
  USERNAME_NULL(10003, "用户名空"),
  PASSWORD_NULL(10004, "密码空"),
  USER_NOT_EXISTS(10005, "用户不存在"),
  BALANCE_NOT_ENOUGH(10006, "余额不足"),
  VALUE_CAN_NOT_BE_NULL(10007,"转账不能为0"),
  CAN_NOT_TRANSFER_TO_SELF(10008, "不能转账给自己"),
  NO_ONLINE_ACCOUNT(10009, "没有创建在线子钱包"),
  NO_ETH_WALLET(10010, "没有创建交易账号"),
  NO_ENERGY_POINT_ACCOUNT(10011, "没有积分帐户"),
  ALREADY_CHECKIN_TODAY(10012, "已经签到"),
  MULTIPLE_TRANSFER_FAILURE(10013, "多路转账失败"),
  WRONG_ADDRESS(10014, "地址不正确，请检查"),
  WRONG_AMOUNT(10015, "金额不正确，请检查"),
  SELECT_THE_CONTRACT(10016, "请选择币种"),
  RATE_NOT_ACCEPTABLE(10017,"汇率出错"),
  NO_AVAILABLE_POINT(10018,"没有相应的积分"),
  NO_DATE_SPECIFIED(10019,"没有指定日期"),
  NO_AVAILABLE_EXCHANGE_RATE(10020,"没有指定汇率"),
  NO_TIME_SPECIFIED(10021,"没有指定时间"),
  NO_CURRENCY_SPECIFIED(10022,"没有指定货币"),
  NO_BLOCKCHAIN_NETWORK(10023,"没有指定区块网络"),
  INVALID_BLOCKCHAIN_NETWORK(10024,"无效的区块网络"),
  REWARD_ALREADY_EXIST(10026,"该奖励配置已存在"),
  IS_BACKUPS_WALLET(10025,"已备份过钱包"),
  mobile_ALREADY_EXISTS(10026, "手机号已存在"),
  email_ALREADY_EXISTS(10027, "邮箱已存在");
  @Getter @Setter private Integer code;
  @Getter @Setter private String message;

  private ErrorCode(Integer code, String message) {
      this.code = code;
      this.message = message;
  }
}
