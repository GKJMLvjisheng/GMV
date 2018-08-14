package com.cascv.oas.core.model;

import com.cascv.oas.core.common.BaseEntity;
import com.cascv.oas.core.model.OnlineSession.OnlineStatus;


import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

/**
 * 当前在线会话 sys_user_online
 */
public class UserOnline extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private String sessionId;	    /** 用户会话id */
    @Getter @Setter private String loginName;
    @Getter @Setter private String startTime;		/** session创建时间 */
    @Getter @Setter private String lastAccessTime;	/** session最后访问时间 */
    @Getter @Setter private Long expireTime;		/** 超时时间，单位为分钟 */
    @Getter @Setter private OnlineStatus status = OnlineStatus.on_line;/** 在线状态 */
    @Getter @Setter private OnlineSession session;	/** 备份的当前用户会话 */

    
    // session对象
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
