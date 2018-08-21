package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

public class BonusPoint implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter private Integer id;
  @Getter @Setter private Integer useId;
  @Getter @Setter private Integer balance;
  @Getter @Setter private Integer power;
  @Getter @Setter private String created;
  @Getter @Setter private String updated;
}
