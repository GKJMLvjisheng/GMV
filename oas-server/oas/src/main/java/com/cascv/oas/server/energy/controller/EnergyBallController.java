package com.cascv.oas.server.energy.controller;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.vo.EnergyPointCheckinResult;
import com.cascv.oas.server.energy.vo.EnergyPointAndPower;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.EnergyBallWithTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/api/energyPoint")
public class EnergyBallController {
    @Autowired
    private EnergyService energyService;
    private static final Integer HAVE_ENERGYBALLS = 1;
    private static final Integer HAVE_NO_ENERGYBALLS = 0;

    /**
     * 签到功能
     * @param userId
     * @return
     */
    @PostMapping(value="/checkin")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> checkin(Integer userId) {
//        EnergyPointCheckinResult energyPointCheckinResult = new EnergyPointCheckinResult();
//        energyPointCheckinResult = energyService.getCurrentEnergyResult(userId);
//        Boolean checkin = (Boolean) this.isCheckin(userId).getData();
//        if (checkin){
//            EnergyPointAndPower checkinEnergy = energyService.getCheckinEnergy();
//            energyPointCheckinResult.setNewPower(energyPointCheckinResult.getNewPower() + checkinEnergy.getPower());
//            energyPointCheckinResult.setNewEnergyPoint(energyPointCheckinResult.getNewEnergyPoint() + checkinEnergy.getPoint());
//            return new ResponseEntity
//                    .Builder<EnergyPointCheckinResult>()
//                    .setData(energyPointCheckinResult)
//                    .setErrorCode(ErrorCode.SUCCESS)
//                    .build();
//        }
//        return new ResponseEntity
//                .Builder<EnergyPointCheckinResult>()
//                .setData(energyPointCheckinResult)
//                .setErrorCode(ErrorCode.GENERAL_ERROR)
//                .build();
        return null;
    }

    /**
     * 当日已签到返回false，当日尚未签到返回true
     * @param userId
     * @return
     */
    @PostMapping(value = "/isCheckin")
    @ResponseBody
    public ResponseEntity<?> isCheckin(Integer userId){
        Boolean checkin = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date());
        EnergyBallWithTime energyBallWithTime = new EnergyBallWithTime(userId, time);
        EnergyBall energyBall = energyService.getEnergyBallByFuzzyTime(energyBallWithTime);
        checkin = energyBall == null ? true : false;
        return new ResponseEntity.Builder<Boolean>().setData(checkin).setErrorCode(ErrorCode.SUCCESS).build();
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
