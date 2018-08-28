package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import com.cascv.oas.server.utils.ShiroUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EnergyBallServiceTest {
    @Autowired
    private EnergyBallService energyBallService;
    @Autowired
    private  EnergyService energyService;
    @Test
    public void saveEnergyBall() {
        EnergyBall energyBall = new EnergyBall();
        energyBall.setUuid("1");
        energyBall.setPointSource(1);
        energyBall.setPowerSource(1);
        energyBall.setStatus(1);
        EnergyCheckinResult checkinEnergy = energyService.getCheckinEnergy();
        energyBall.setPoint(checkinEnergy.getNewEnergyPoint());
        energyBall.setPower(checkinEnergy.getNewPower());
        String time = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        energyBall.setTimeUpdated(time);
        energyBall.setTimeCreated(time);
        int i = energyBallService.saveEnergyBall(energyBall);
        if(i >= 0) {
            System.out.println("测试成功，插入成功！");
        }
    }

    @Test
    public void updateEnergyBallStatusById() {
        int i = energyBallService.updateEnergyBallStatusById("1");
        System.out.println("i: " + i);
    }

    @Test
    public void getEnergyBallById() {
    }

    @Test
    public void listEnergyBallByStatus() {
    }
}