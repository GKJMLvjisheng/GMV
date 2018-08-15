package com.cascv.oas.server.user.service;

import java.util.Date;
import java.util.List;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.StringUtils;
import com.cascv.oas.server.user.model.UserOnline;
import com.cascv.oas.server.shiro.OnlineSessionDAO;
import com.cascv.oas.server.user.mapper.UserOnlineMapper;


/**
 */
@Service
public class UserOnlineService {
	@Autowired
    private UserOnlineMapper userOnlineDao;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    public UserOnline selectOnlineById(String sessionId)
    {
        return userOnlineDao.selectOnlineById(sessionId);
    }

    public void deleteOnlineById(String sessionId)
    {
        UserOnline userOnline = selectOnlineById(sessionId);
        if (StringUtils.isNotNull(userOnline))
        {
            userOnlineDao.deleteOnlineById(sessionId);
        }
    }

    
    public void batchDeleteOnline(List<String> sessions)
    {
        for (String sessionId : sessions)
        {
            UserOnline userOnline = selectOnlineById(sessionId);
            if (StringUtils.isNotNull(userOnline))
            {
                userOnlineDao.deleteOnlineById(sessionId);
            }
        }
    }

    public void saveOnline(UserOnline online)
    {
    	userOnlineDao.saveOnline(online);
    }

    public List<UserOnline> selectUserOnlineList(UserOnline userOnline)
    {
        return userOnlineDao.selectUserOnlineList(userOnline);
    }

    public void forceLogout(String sessionId)
    {
        Session session = onlineSessionDAO.readSession(sessionId);
        if (session == null) {
            return;
        }
        session.setTimeout(1000);
        userOnlineDao.deleteOnlineById(sessionId);
    }

    
    public List<UserOnline> selectOnlineByExpired(Date expiredDate)
    {
        String lastAccessTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, expiredDate);
        return userOnlineDao.selectOnlineByExpired(lastAccessTime);
    }
}
