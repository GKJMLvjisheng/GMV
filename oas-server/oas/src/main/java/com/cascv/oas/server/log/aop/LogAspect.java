package com.cascv.oas.server.log.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.log.annotation.WriteLog;
import com.cascv.oas.server.log.model.LogModel;
import com.cascv.oas.server.log.service.LogService;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Order(1)
@Component
@Slf4j
public class LogAspect {
  
  @Autowired
  private LogService logService;
  
  @After(("execution(* com.cascv.oas.server..*.*(..)) && @annotation(writeLog)"))
  public void doAfterAdvice(JoinPoint joinPoint, WriteLog writeLog) {  
    String value = writeLog.value();
    LogModel logModel = new LogModel();
    logModel.setAction(value);
    logModel.setTime(DateUtils.getTime());
    logModel.setSessionId(ShiroUtils.getSessionId());
    logModel.setHost(ShiroUtils.getIp());
    logModel.setUser(ShiroUtils.getLoginName());
    logModel.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.LOG));
    logService.addLog(logModel);
    log.info("## {} {}", ShiroUtils.getLoginName(), value);
  }
}
