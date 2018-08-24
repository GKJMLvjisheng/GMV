package com.cascv.oas.server.energy.controller;

import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.EnergyBallWithTimeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("energyPoint")
public class EnergyBallController {
    @Autowired
    private EnergyService energyService;
    // 存在符合条件的能量球
    private static final Integer HAVE_ENERGYBALLS = 1;
    // 不存在符合条件的能量球
    private static final Integer HAVE_NO_ENERGYBALLS = 0;

    @RequestMapping("judge")
    public String Judge(Integer userId) {
        // 产生格式化的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date());
        // 创建vo对象
        EnergyBallWithTimeVo energyBallWithTimeVo = new EnergyBallWithTimeVo(userId, time);
//        List<EnergyBall> energyBalls = energyService.getEnergyBallByFuzzyTime(energyBallWithTimeVo);
        return null;
    }
}
