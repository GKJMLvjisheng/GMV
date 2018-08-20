package com.cascv.oas.server.blockchain.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.model.EnergyBall;
import com.cascv.oas.server.blockchain.vo.BonusPointCheckinResult;
import com.cascv.oas.server.blockchain.vo.EnergyBallResult;
import com.cascv.oas.server.blockchain.vo.EnergyBallTakenResult;

@RestController
@RequestMapping(value="/api/v1/bonusPoint")
public class BonusPointController {
  

  
  
  @PostMapping(value="/checkin")
  public ResponseEntity<?> checkin(){
    BonusPointCheckinResult bonusPointCheckinResult = new BonusPointCheckinResult();
    bonusPointCheckinResult.setNewBonusPoint(15);
    bonusPointCheckinResult.setNewPower(10);
    return new ResponseEntity.Builder<BonusPointCheckinResult>().setData(bonusPointCheckinResult).setStatus(0).setMessage("成功").build();
  }
  
  
  @PostMapping(value="/inquireEnergyBall")
  public ResponseEntity<?> inquireEnergyBall(){
    EnergyBallResult energyBallResult = new EnergyBallResult();
    List<EnergyBall> energyBallList = new ArrayList<>();
    for (Integer i = 0; i < 16; i++) {
      EnergyBall energyBall = new EnergyBall();
      energyBall.setId(i+1);
      energyBall.setType(1);
      energyBall.setValue(i + 6);
      energyBall.setName("daily");
      energyBall.setStartDate(DateUtils.getDate());
      energyBall.setEndDate(DateUtils.getDate());
      energyBallList.add(energyBall);
      
    }
    energyBallResult.setEnergyBallList(energyBallList);
    energyBallResult.setOngoingEnergySummary(236);
    return new ResponseEntity.Builder<EnergyBallResult>().setData(energyBallResult).setStatus(0).setMessage("成功").build();
  }
  
  @PostMapping(value="/takeEnergyBall")
  public ResponseEntity<?> takeEnergyBall(Integer ballId){
    EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
    energyBallTakenResult.setNewBonusPonit(15);
    energyBallTakenResult.setNewPower(0);
    return new ResponseEntity.Builder<EnergyBallTakenResult>().setData(energyBallTakenResult).setStatus(0).setMessage("成功").build();
  }
  
  @PostMapping(value="/inquirePower")
  public ResponseEntity<?> inquirePower(){
    return new ResponseEntity.Builder<Integer>().setData(10).setStatus(0).setMessage("成功").build();
  }
  
  @PostMapping(value="/inquireBonusPoint")
  public ResponseEntity<?> inquireBonusPoint(){
    return new ResponseEntity.Builder<Integer>().setData(15).setStatus(0).setMessage("成功").build();
  }
}
