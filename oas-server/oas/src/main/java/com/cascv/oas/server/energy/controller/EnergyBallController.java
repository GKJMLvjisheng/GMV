package com.cascv.oas.server.energy.controller;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.vo.EnergyPointCheckinResult;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.service.EnergyBallService;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.service.UserEnergyService;
import com.cascv.oas.server.energy.vo.EnergyBallWithTime;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import org.apache.poi.ddf.EscherBSERecord;
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
    @Autowired
    private EnergyBallService energyBallService;
    @Autowired
    private UserEnergyService userEnergyService;


    /**
     * 签到功能
     * @param userId
     * @return
     */
    @PostMapping(value="/checkin")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> checkin(Integer userId) {
        // 检查当日是否已经签过到
        Boolean checkin = energyService.isCheckin(userId);
        if (checkin){
            // 在数据库中生成签到“能量球”
            EnergyBall energyBallCheckin = energyService.setEnergyBallCheckin(userId);
            int energyBallId = energyBallService.saveEnergyBall(energyBallCheckin);
            // 添加用户能量记录
            EnergyCheckinResult energyCheckinResult = userEnergyService.saveUserEnergy(userId, energyBallId);
            // 更改签到“能量球”状态，1 -> 0,表示能量已被获取
            energyBallService.updateEnergyBallStatusById(userId);
            return new ResponseEntity
                    .Builder<EnergyCheckinResult>()
                    .setData(energyCheckinResult)
                    .setErrorCode(ErrorCode.SUCCESS)
                    .build();
        }else {
            // 已经签过到，不允许再次签到
            return new ResponseEntity
                    .Builder<Integer>()
                    .setData(0)
                    .setErrorCode(ErrorCode.GENERAL_ERROR)
                    .build();
        }
    }


}
