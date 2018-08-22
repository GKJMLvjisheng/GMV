package com.cascv.oas.server.shiro;

import java.io.Serializable;
import java.util.Date;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cascv.oas.server.user.model.OnlineSession;
import com.cascv.oas.server.user.model.UserOnline;
import com.cascv.oas.server.user.service.UserOnlineService;

import lombok.extern.slf4j.Slf4j;


// 
@Slf4j
public class OnlineSessionDAO extends EnterpriseCacheSessionDAO
{
    
    @Value("${shiro.session.dbSyncPeriod}")
    private int dbSyncPeriod;
    
    private static final String LAST_SYNC_DB_TIMESTAMP = OnlineSessionDAO.class.getName() + "LAST_SYNC_DB_TIMESTAMP";

    @Autowired
    private UserOnlineService onlineService;

    @Autowired
    private OnlineSessionFactory onlineSessionFactory;

    public OnlineSessionDAO() {
        super();
    }

    public OnlineSessionDAO(long expireTime) {
        super();
    }

    
    // 
    @Override
    protected Session doReadSession(Serializable sessionId) {
        UserOnline userOnline = onlineService.selectOnlineById(String.valueOf(sessionId));
        log.info("doReadSession {}", String.valueOf(sessionId));
        
        if (userOnline == null) {
        	log.info("doReadSession {} null", String.valueOf(sessionId));
        	return null;
        }
        return onlineSessionFactory.createSession(userOnline);
    }
    
    // 
    public void syncToDb(OnlineSession onlineSession) {
      Date lastSyncTimestamp = (Date) onlineSession.getAttribute(LAST_SYNC_DB_TIMESTAMP);
      if (lastSyncTimestamp != null) {
        boolean needSync = true;
            long deltaTime = onlineSession.getLastAccessTime().getTime() - lastSyncTimestamp.getTime();
            if (deltaTime < dbSyncPeriod * 60 * 1000) {
                // 
                needSync = false;
            }
            boolean isGuest = onlineSession.getUserUuid() == null;
            // 
            if (isGuest == false && onlineSession.isAttributeChanged()) {
                needSync = true;
            }
            if (needSync == false) {
                return;
            }
        }
        onlineSession.setAttribute(LAST_SYNC_DB_TIMESTAMP, onlineSession.getLastAccessTime());
        // 
        if (onlineSession.isAttributeChanged())  {
            onlineSession.resetAttributeChanged();
        }
        onlineService.saveOnline(UserOnline.fromOnlineSession(onlineSession));
    }

    
    // 
    @Override
    protected void doDelete(Session session) {
	    OnlineSession onlineSession = (OnlineSession) session;
	    if (null == onlineSession)  {
	        return;
	    }
	    onlineSession.setStatus(OnlineSession.OnlineStatus.off_line);
	    onlineService.deleteOnlineById(String.valueOf(onlineSession.getId()));
    }
}
