package com.cascv.oas.server.blockchain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EnergyNews implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter private Integer id;
  @Getter @Setter private String title;
  @Getter @Setter private String summary;
  @Getter @Setter private String imageLink;
  @Getter @Setter private String newsLink;  
}
