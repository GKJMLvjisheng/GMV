package com.cascv.oas.server.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cascv.oas.server.common.ShiroConstants;
import com.cascv.oas.server.user.service.UserOnlineService;
import com.cascv.oas.server.user.model.OnlineSession;
import com.cascv.oas.server.user.model.UserOnline;
import com.cascv.oas.core.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

// 
@Slf4j
public class OnlineWebSessionManager extends DefaultWebSessionManager
{
	private static final String HEADER_TOKEN_NAME = "token";
	private static final String REFERENCED_SESSION_ID_SOURCE = "appStatelessRequest";
	
    @Autowired
    private UserOnlineService userOnlineService;
    
    @Override
    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) 
          throws InvalidSessionException {
      super.setAttribute(sessionKey, attributeKey, value);
      if (value != null && needMarkAttributeChanged(attributeKey)){
        OnlineSession s = (OnlineSession) doGetSession(sessionKey);
        s.markAttributeChanged();
      }
    }

    private boolean needMarkAttributeChanged(Object attributeKey) {
      if (attributeKey == null) {
        return false;
      }
      String attributeKeyStr = attributeKey.toString();
      if (attributeKeyStr.startsWith("org.springframework")){
        return false;
      }
      if (attributeKeyStr.startsWith("javax.servlet")){
        return false;
      }
      if (attributeKeyStr.equals(ShiroConstants.CURRENT_USERNAME)) {
        return false;
      }
      return true;
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) 
        throws InvalidSessionException {
        Object removed = super.removeAttribute(sessionKey, attributeKey);
        if (removed != null) {
            OnlineSession s = (OnlineSession) doGetSession(sessionKey);
            s.markAttributeChanged();
        }
        return removed;
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response){
    	HttpServletRequest httpRequest = WebUtils.toHttp(request); 
        String id = httpRequest.getHeader(HEADER_TOKEN_NAME);
        String uri = httpRequest.getRequestURI();
        if(StringUtils.isEmpty(id)){
          Serializable ret = super.getSessionId(request, response);
          if (ret != null) {
             log.info("{} has no 'token' and use {}", uri, ret.toString());
          } else {
             log.info("{} has no 'token' and use null", uri);
          }
          return ret;
        }else{
       	  request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,REFERENCED_SESSION_ID_SOURCE);
          request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,id);
          request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
          log.info("{} has 'token' {}", uri, id);
          return id;
        }
    }
    

    @Override
    public void validateSessions()
    {
      int invalidCount = 0;
      int timeout = (int) this.getGlobalSessionTimeout();
      Date expiredDate = DateUtils.addMilliseconds(new Date(), 0 - timeout);
      List<UserOnline> userOnlineList = userOnlineService.selectOnlineByExpired(expiredDate);
      // 
      List<String> needOfflineIdList = new ArrayList<String>();
      for (UserOnline userOnline : userOnlineList) {
        try {
          SessionKey key = new DefaultSessionKey(userOnline.getSessionId());
          Session session = retrieveSession(key);
          if (session != null) {
            throw new InvalidSessionException();
          }
        } catch (InvalidSessionException e) {
          if (log.isDebugEnabled()) {
            boolean expired = (e instanceof ExpiredSessionException);
            String s = expired ? " (expired)" : " (stopped)";
            log.debug("Invalidated session with id [{}] {}", userOnline.getSessionId(), s);
          }
          invalidCount++;
          log.info("{} invalidate session {}", invalidCount, userOnline.getSessionId());
          needOfflineIdList.add(userOnline.getSessionId());
        }
      }
      if (needOfflineIdList.size() > 0)  {
        try {
          userOnlineService.batchDeleteOnline(needOfflineIdList);
        } catch (Exception e) {
          log.error("batch delete db session error.", e);
        }
      }

      if (log.isInfoEnabled()) {
        if (invalidCount > 0) {
            log.info("Finished invalidation session.[{}] sessions were stopped.", 
            invalidCount);
        } else {
          log.info("Finished invalidation session. No sessions were stopped.");
        }
      }
    }

  @Override
  protected Collection<Session> getActiveSessions() {
    throw new UnsupportedOperationException("getActiveSessions method not supported");
  }
}
