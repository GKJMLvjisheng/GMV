package com.cascv.oas.server.user.model;

import com.cascv.oas.core.common.BaseEntity;
import com.cascv.oas.server.user.model.OnlineSession.OnlineStatus;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

/**
 */
public class UserOnline extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private String sessionId;
    @Getter @Setter private String loginName;
    @Getter @Setter private String startTime;		
    @Getter @Setter private String lastAccessTime;	
    @Getter @Setter private Long expireTime;		
    @Getter @Setter private OnlineStatus status = OnlineStatus.on_line;
    @Getter @Setter private OnlineSession session;	

    
    public static final UserOnline fromOnlineSession(OnlineSession session) {
        UserOnline online = new UserOnline();
        online.setSessionId(String.valueOf(session.getId()));
        online.setLoginName(session.getLoginName());
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        online.setStartTime(formatter.format(session.getStartTimestamp()));
        online.setLastAccessTime(formatter.format(session.getLastAccessTime()));
        
        online.setExpireTime(session.getTimeout());
        online.setStatus(session.getStatus());
        online.setSession(session);
        return online;
    }



    @Override
    public String toString() {
      return "UserOnline [sessionId=" + sessionId + ", loginName=" + loginName
        + ", startTime=" + startTime + ", lastAccessTime=" + lastAccessTime 
        + ", expireTime=" + expireTime + ", status=" + status + ", session=" + session + "]";
    }
}
