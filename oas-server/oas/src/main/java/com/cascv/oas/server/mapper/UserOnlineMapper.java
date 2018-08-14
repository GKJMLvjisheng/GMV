package com.cascv.oas.server.mapper;

import java.util.List;
import com.cascv.oas.core.model.UserOnline;

// 在线用户 数据层
public interface UserOnlineMapper
{
    // 通过会话序号查询信息
    public UserOnline selectOnlineById(String sessionId);

    // 通过会话序号删除信息
    public int deleteOnlineById(String sessionId);

    // 保存会话信息
    public int saveOnline(UserOnline online);

    // 查询会话集合
    public List<UserOnline> selectUserOnlineList(UserOnline userOnline);

    // 查询过期会话集合
    public List<UserOnline> selectOnlineByExpired(String lastAccessTime);
}
