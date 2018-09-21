package com.cascv.oas.server.log.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class LogEntry implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String user;
  @Getter @Setter private String time;
  @Getter @Setter private String action;
  @Getter @Setter private String comment;
}
