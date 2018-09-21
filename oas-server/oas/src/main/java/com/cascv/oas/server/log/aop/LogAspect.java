package com.cascv.oas.server.log.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.log.annotation.WriteLog;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Order(1)
@Component
@Slf4j
public class LogAspect {
  @After(("execution(* com.cascv.oas.server..*.*(..)) && @annotation(writeLog)"))
  public void doAfterAdvice(JoinPoint joinPoint, WriteLog writeLog) {  
    String value = writeLog.value();
    log.info("======={}", value);
  }
}
