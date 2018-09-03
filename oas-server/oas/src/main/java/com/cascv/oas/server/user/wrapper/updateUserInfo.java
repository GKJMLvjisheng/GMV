package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class updateUserInfo implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter String EuserNickname;
  @Getter @Setter String EuserName;
  @Getter @Setter String EuserGender;
  @Getter @Setter String EuserBirthday;
  @Getter @Setter String EuserAddress;
  void toSring(){	  
  }
}