package com.cascv.oas.server.news.config;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class MediaServer implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String imageHost;
}
