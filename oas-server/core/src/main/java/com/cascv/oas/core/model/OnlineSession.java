package com.cascv.oas.core.model;

import org.apache.shiro.session.mgt.SimpleSession;

import lombok.Getter;
import lombok.Setter;

// * 在线会话
public class OnlineSession extends SimpleSession {

  private static final long serialVersionUID = 1L;

  @Setter @Getter private Long userId;      // 用户ID 
  @Setter @Getter private String loginName; // 用户名
  @Setter @Getter private OnlineStatus status = OnlineStatus.on_line;//状态

  // 属性是否改变,session数据同步
  private transient boolean attributeChanged = false;

  public void markAttributeChanged() {
      this.attributeChanged = true;
  }

  public void resetAttributeChanged() {
      this.attributeChanged = false;
  }

  public boolean isAttributeChanged() {
      return attributeChanged;
  }

  @Override
  public void setAttribute(Object key, Object value) {
      super.setAttribute(key, value);
  }

  @Override
  public Object removeAttribute(Object key) {
      return super.removeAttribute(key);
  }

  public static enum OnlineStatus {
    on_line("在线"), off_line("离线");
    private final String info;

    private OnlineStatus(String info){
      this.info = info;
    }

    public String getInfo(){
      return info;
    }
  }

}
