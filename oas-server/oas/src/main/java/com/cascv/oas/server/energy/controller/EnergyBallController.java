package com.cascv.oas.server.energy.controller;

import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.EnergyBallWithTime;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/api/energyPoint")
public class EnergyBallController {
    @Autowired
    private EnergyService energyService;
    private static final Integer HAVE_ENERGYBALLS = 1;
    private static final Integer HAVE_NO_ENERGYBALLS = 0;

    @PostMapping(value="/checkin")
    @ResponseBody
    @Transactional
    public String checkin(Integer userId) {
        if (this.isCheckin(userId)){

        }
        return null;
    }

    /**
     * 当日已签到返回false，当日尚未签到返回true
     * @param userId
     * @return
     */
    @PostMapping(value = "/isCheckin")
    @ResponseBody
    public boolean isCheckin(Integer userId){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date());
        EnergyBallWithTime energyBallWithTime = new EnergyBallWithTime(userId, time);
        EnergyBall energyBall = energyService.getEnergyBallByFuzzyTime(energyBallWithTime);
        return energyBall == null ? true : false;
    }

    @PostMapping(value="/inquireEnergyBall")
    @ResponseBody
    public List<EnergyBall> inquireEnergyBall() {
        return null;
    }

    @PostMapping(value = "/takeEnergyBall")
    @ResponseBody
    @Transactional
    public String takeEnergyBall() {
        List<EnergyBall> energyBalls = energyService.listEnergyBallByStatus();
        return null;
    }
}
