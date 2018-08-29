package com.cascv.oas.server.energy.controller;

import com.cascv.oas.core.common.ResponseEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EnergyPointControllerTest {
    @Autowired
    private EnergyPointController energyPointController;
    @Test
    public void checkin() {
        ResponseEntity<?> checkin = energyPointController.checkin("USR-0178ea59a6ab11e883290a1411382ce0");
        System.out.println("data: " + checkin.getData());
        System.out.println("code: " + checkin.getCode());
        System.out.println("message: " + checkin.getMessage());
    }
}