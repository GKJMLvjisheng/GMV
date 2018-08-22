package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EnergyPoint implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String uuid;
  @Getter @Setter private String userUuid;
  @Getter @Setter private Integer balance;
  @Getter @Setter private Integer power;
  @Getter @Setter private String created;
  @Getter @Setter private String updated;
}
