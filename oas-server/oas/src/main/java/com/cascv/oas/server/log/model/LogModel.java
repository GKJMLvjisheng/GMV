package com.cascv.oas.server.log.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class LogModel implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String uuid;
  @Getter @Setter private String user;
  @Getter @Setter private String host;
  @Getter @Setter private String sessionId;
  @Getter @Setter private String action;
  @Getter @Setter private String comment;
  @Getter @Setter private String time;
}
