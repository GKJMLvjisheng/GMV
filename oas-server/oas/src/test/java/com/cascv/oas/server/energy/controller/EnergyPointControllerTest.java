package com.cascv.oas.server.energy.controller;

import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.energy.vo.EnergyBallTokenRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//USR-9590d7f9a5c811e883290a1411382ce0
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EnergyPointControllerTest {
    @Autowired
    private EnergyPointController energyPointController;
    @Test
    public void checkin() {
        ResponseEntity<?> checkin = energyPointController.checkin();
        System.out.println("data: " + checkin.getData());
        System.out.println("code: " + checkin.getCode());
        System.out.println("message: " + checkin.getMessage());
    }

    @Test
    public void inquireEnergyBall() {
        ResponseEntity<?> energyBallResult = energyPointController.inquireEnergyPointBall();
        System.out.println("data: " + energyBallResult.getData());
        System.out.println("code: " + energyBallResult.getCode());
        System.out.println("message: " + energyBallResult.getMessage());
    }

    @Test
    public void takeEnergyBall() {
        EnergyBallTokenRequest energyBallTokenRequest = new EnergyBallTokenRequest();
        energyBallTokenRequest.setBallId("EP-e7606aa2b95e11e898b93052cb119add");
        ResponseEntity<?> responseEntity = energyPointController.takeEnergyPointBall(energyBallTokenRequest);
        System.out.println("data: " + responseEntity.getData());
        System.out.println("code: " + responseEntity.getCode());
        System.out.println("message: " + responseEntity.getMessage());
    }
}