package com.cascv.oas.server.energy.service;

import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.EnergyBallWithTimeVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EnergyServiceTest {
    @Autowired
    private EnergyService energyService;

    @Test
    public void getEnergyBallByFuzzyTime() {
        // userID
        Integer userId = 1;
        // 创建 vo 对象 EnergyBallWithTime
        String time = "2018-08-22";
        EnergyBallWithTimeVo energyBallWithTimeVo = new EnergyBallWithTimeVo(userId, time);
        //  测试 energyService.方法
        EnergyBall energyBall = energyService.getEnergyBallByFuzzyTime(energyBallWithTimeVo);
        System.out.println("energyBalls:" + energyBall.toString());
    }
}