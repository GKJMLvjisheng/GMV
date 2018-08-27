package com.cascv.oas.server.energy.service;

import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.vo.EnergyPointAndPower;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EnergyServiceTest {
    @Autowired
    private EnergyService energyService;
    @Test
    public void listEnergyBallByStatus() {
    }

    @Test
    public void getCheckinEnergy() {
        UserEnergy checkinEnergy = energyService.getCheckinEnergy();
        System.out.println("checkinEnergy: " + checkinEnergy);
    }

    @Test
    public void getCurrentEnergyResult() {
    }
}