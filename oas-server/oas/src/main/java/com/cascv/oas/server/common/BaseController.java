package com.cascv.oas.server.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSON;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.utils.ServletUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class BaseController {
  @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
  public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
      if (ServletUtils.isAjaxRequest(request)) {
        ResponseEntity<?> responseEntity = new ResponseEntity.Builder<String>()
              .setData("unauthenticated access")
              .setErrorCode(ErrorCode.UNAUTHENTICATED_ACCESS).build();
        writeJson(responseEntity, response);
        log.info("UnauthenticatedException ajax");
        return null;
      } else {
        log.info("UnauthenticatedException not ajax");
        return "redirect:login";
      }
  }
  @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
  public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
      if (ServletUtils.isAjaxRequest(request)) {
        ResponseEntity<?> responseEntity = new ResponseEntity.Builder<String>()
            .setData("unauthorizated access")
            .setErrorCode(ErrorCode.UNAUTHORIZATED_ACCESS).build();
          writeJson(responseEntity, response);
          log.info("UnauthorizedException ajax");
          return null;
      } else {
          log.info("UnauthorizedException not ajax");
          return "redirect:login";
      }
  }
  private void writeJson(ResponseEntity<?> responseEntity, HttpServletResponse response) {
    PrintWriter out = null;
    try {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        out = response.getWriter();
        out.write(JSON.toJSONString(responseEntity));
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (out != null) {
            out.close();
        }
    }
  }
}
