package com.cascv.oas.server.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.utils.ServletUtils;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KickoutSessionControlFilter extends AccessControlFilter {
  @Getter @Setter private String kickoutUrl;      //踢出后的地址
  @Getter @Setter private int maxSession = 1;     //同一个账号会话数
  @Getter @Setter private SessionManager sessionManager;
  @Getter @Setter private Cache<String, Deque<Serializable>> cache;
  
  //isAccessAllowed表示是否允许访问;mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false
  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
      throws Exception {
    // 
    return false;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    Subject subject = getSubject(request, response);
    
    if (!subject.isAuthenticated() && !subject.isRemembered()) {
      // 如果没有登录，直接进行之后的流程
      return true;
    }
    Session session = ShiroUtils.getSession();
    UserModel userModel;
    Object principal = subject.getPrincipal();
    if (subject.getPrincipal() instanceof UserModel) {
      userModel = (UserModel) principal;
      log.info("principal was from userModel");
    } else {
      String temp = JSON.toJSON(principal).toString();
      userModel = JSON.parseObject(temp, UserModel.class);
      log.info("principal was from JSON parser");
    }
    String username = userModel.getName();
    Serializable sessionId = session.getId();
    log.info("user session {}", sessionId.toString());
    // 
    Deque<Serializable> deque = cache.get(username);

    // no queue for user session in cache
    // create one 
    if (deque == null) {
      deque = new LinkedList<Serializable>();
      log.info("create user session queue");
    }

    // 如果队列里没有此sessionId，且用户没有被踢出；放入队列
    if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
      // 将sessionId存入队列
      log.info("session {} to queue", sessionId.toString());
      deque.push(sessionId);
      // 将用户的sessionId队列缓存
      cache.put(username, deque);
    }
    log.info("user {} queue size {}", username, deque.size());
    // 
    while (deque.size() > maxSession) {
      Serializable kickoutSessionId = null;
        
        kickoutSessionId = deque.removeLast();
        log.info("kickout {} (removeLast)", kickoutSessionId);
        // update queue in cache
        cache.put(username, deque);
      try {
        // 获取被踢出的sessionId的session对象
        Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
        if (kickoutSession != null) {
          // 设置会话的kickout属性表示踢出了
          kickoutSession.setAttribute("kickout", true);
          log.info("set session to kickout");
        }
      } catch (Exception e) {// ignore exception
      }
    }

    // 如果被踢出了，直接退出，重定向到踢出后的地址
    if ((Boolean) session.getAttribute("kickout") != null 
        && (Boolean) session.getAttribute("kickout") == true) {
      // session kickout
      try {
        // 退出登录
        subject.logout();
      } catch (Exception e) { // ignore
      }
      saveRequest(request);
      Map<String, String> resultMap = new HashMap<String, String>();
      // ajax
      if (ServletUtils.isAjaxRequest((HttpServletRequest) request)) {
        log.info("is ajax req");
        resultMap.put("code", "300");
        resultMap.put("msg", "您已经在其他地方登录，请重新登录！");
        // 输出json串
        out(response, resultMap);
      } else {
        log.info("redirect to {}", kickoutUrl);
        WebUtils.issueRedirect(request, response, kickoutUrl);
      }
      return false;
    }
    return true;
  }
    private void out(ServletResponse hresponse, Map<String, String> resultMap) throws IOException {
    try {
      hresponse.setCharacterEncoding("UTF-8");
      PrintWriter out = hresponse.getWriter();
      out.println(JSON.toJSONString(resultMap));
      out.flush();
      out.close();
    } catch (Exception e) {
      System.err.println("KickoutSessionFilter.class exception, ignore it。");
    }
  }
}
