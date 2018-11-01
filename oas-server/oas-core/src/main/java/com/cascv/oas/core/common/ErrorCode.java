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
  email_ALREADY_EXISTS(10027, "邮箱已存在"),
  wechat_ALREADY_EXISTS(10028, "不要重复输入验证码!"),
  identifCode_ERROR(10029, "验证码错误！"),
  UPDATE_FAILED(10030,"更新失败"),
  INPUT_ILLEGAL(10031,"请求的参数不符合要求"),
  SELECT_EMPTY(10032,"查询不到该记录"),
  OAS_EVENT_HAVE_HANDLED(10033,"该提币记录管理员已处理"),
  SYSTEM_NOT_EXIST(10034,"SYSTEM用户不存在"),
  UNCONFIRMED_BALANCE(10035,"待确认交易余额不足，无法扣除"),
  OAS_EXTRA_MONEY_NOT_ENOUGH(10036,"余额不足，手续费无法扣除"),
  ETH_RETURN_HASH(10037,"交易钱包返回hash值为空"),
  UNLOGIN_FAILED(10038,"该用户未登录"),
  SEND_SMS_ERROR(10039,"给管理员发送信息失败"),
  UNAUTHENTICATED_ACCESS(10040,"未认证的访问"),
  UNAUTHORIZATED_ACCESS(10041,"未授权的访问"),
  NO_WALK_PERMISSION(10042,"在当前时间段，计步奖励功能暂时无法使用。欲知详情，请参见社区公告或联系系统管理员：oases@oases.pro"),
  No_TRANSFER_PERMISSION(10043,"在当前时间段，转账功能暂时无法使用。欲知详情，请参见社区公告或联系系统管理员：oases@oases.pro"),
  No_REVERSEWITHDRAW_PERMISSION(10043,"在当前时间段，充币功能暂时无法使用。欲知详情，请参见社区公告或联系系统管理员：oases@oases.pro"),
  No_WITHDRAW_PERMISSION(10043,"在当前时间段，提币功能暂时无法使用。欲知详情，请参见社区公告或联系系统管理员：oases@oases.pro"),

  THE_FIRST_ONE(10044, "已是第一个，不能上移"),
  THE_LAST_ONE(10045, "已是最后一个，不能下移"),
  LOGIN_FAILED(10046, "登录失败"),
  SYSTEM_ETH_BALANCE_LOWER_THAN_WARING(10047,"用于提充币的系统交易钱包账号余额已经低于警戒值，请管理员立刻查看原因，并采取行动"),
  SYSTEM_USER_BALANCE_LOWER_THAN_WARING(10048,"用于提充币的系统在线钱包账号余额已经低于警戒值，请管理员立刻查看原因，并采取行动"),
  FIRSTONE_INPUT_NO_ILLEGAL(10049,"输入不符合规范"),
  FIRSTONE_NOT_EXIST(10050,"FIRSTONE用户不存在"),
  INVITECODE_NOT_EXIST(10051,"无此邀请码"),
  INVITECODE_IS_SELF(10052,"此邀请码为自己"),
  USER_REGISTER_NO_ACTIVE(10053,"用户未激活,请前往注册页面完成剩余流程"),
  USER_NO_ACTIVE_BUT_NO_UUID(10054,"该用户未激活却无法获取其用户id"),
  USER_IMEI_REPEAT(10055,"登录失败，该手机已绑定其他账号"),
  USER_PASS_NOT_SAME(10056,"获取失败，密码与之前注册时候使用的密码不同"),
  ALREADY_BACKUP(10057,"已经备份过钱包"),
  SOURCE_UUID_EXSIT(10058, "标识符已存在"),
  USER_IMEI_EXIST(10059, "该用户已经绑定其他手机"),
  USER_CANNOT_LOGIN_IN_ANDROID(10060, "该用户是系统账号,不能在移动端登录"),
  USER_IS_FORBIDDEN(10061, "该用户已经被禁用");
	

	
  @Getter @Setter private Integer code;
  @Getter @Setter private String message;

  private ErrorCode(Integer code, String message) {
      this.code = code;
      this.message = message;
  }
}
