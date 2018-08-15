package com.cascv.oas.server.user.mapper;

import java.util.List;
import com.cascv.oas.server.user.model.UserOnline;

// 
public interface UserOnlineMapper
{
   
    public UserOnline selectOnlineById(String sessionId);

   
    public int deleteOnlineById(String sessionId);

   
    public int saveOnline(UserOnline online);

   
    public List<UserOnline> selectUserOnlineList(UserOnline userOnline);

    public List<UserOnline> selectOnlineByExpired(String lastAccessTime);
}
