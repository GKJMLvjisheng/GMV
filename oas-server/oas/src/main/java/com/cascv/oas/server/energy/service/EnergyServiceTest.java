package com.cascv.oas.server.energy.service;

import com.cascv.oas.server.blockchain.vo.EnergyPointCheckinResult;
import com.cascv.oas.server.energy.model.UserEnergy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        EnergyPointCheckinResult checkinEnergy = energyService.getCheckinEnergy();
        System.out.println("签到增加值: " + checkinEnergy);
    }

    @Test
    public void getCurrentEnergyResult() {
        int userId = 1;
        UserEnergy currentEnergyResult = energyService.getCurrentEnergyResult(userId);
        System.out.println("当前用户能量值：" + currentEnergyResult);
    }
}