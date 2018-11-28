package com.gkyj.gmv.server.user.model;

import org.apache.shiro.session.mgt.SimpleSession;

import lombok.Getter;
import lombok.Setter;

// 
public class OnlineSession extends SimpleSession {

  private static final long serialVersionUID = 1L;

  @Setter @Getter private String userUuid;     
  @Setter @Getter private String loginName; 
  @Setter @Getter private OnlineStatus status = OnlineStatus.on_line;

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
    on_line("online"), off_line("offline");
    private final String info;

    private OnlineStatus(String info){
      this.info = info;
    }

    public String getInfo(){
      return info;
    }
  }

}
