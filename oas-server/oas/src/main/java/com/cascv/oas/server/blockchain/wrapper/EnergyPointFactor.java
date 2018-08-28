package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EnergyPointFactor implements Serializable{

  private static final long serialVersionUID = 1L;
    @Setter @Getter Double factor;
    @Setter @Getter Integer amount;
    @Setter @Getter String date;  //2018-08
}