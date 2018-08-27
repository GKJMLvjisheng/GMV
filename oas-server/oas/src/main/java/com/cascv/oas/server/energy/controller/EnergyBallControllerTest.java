package com.cascv.oas.server.energy.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EnergyBallControllerTest {
    @Autowired
    private EnergyBallController energyBallController;
    @Test
    public void checkin() {
    }

    @Test
    public void isCheckin() {
        Integer userId = 1;
        Boolean ischeckin = energyBallController.isCheckin(userId);
        System.out.println("尚未签到？ " + ischeckin);
    }
}