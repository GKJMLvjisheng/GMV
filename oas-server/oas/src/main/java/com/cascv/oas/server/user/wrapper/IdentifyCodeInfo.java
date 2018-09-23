package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *  
 * @author Ming Yang
 * @20180923
 */
public class IdentifyCodeInfo implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter String identifyCode;
}