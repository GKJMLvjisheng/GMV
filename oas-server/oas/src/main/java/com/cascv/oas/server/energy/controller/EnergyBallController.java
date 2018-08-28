package com.cascv.oas.server.energy.controller;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.service.EnergyBallService;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.service.UserEnergyService;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
    public ResponseEntity<?> checkin(String userId) {
        EnergyCheckinResult energyCheckinResult = new EnergyCheckinResult();
        ErrorCode errorCode = ErrorCode.SUCCESS;

        // 检查当日是否已经签过到
        Boolean checkin = energyService.isCheckin(userId);
        if (!checkin){
            // 在数据库中生成签到记录以供签到
            EnergyBall energyBallCheckin = energyService.setEnergyBallCheckin(userId);
            energyBallService.saveEnergyBall(energyBallCheckin);
            // 添加用户能量记录
            energyCheckinResult = userEnergyService.saveUserEnergy(userId, energyBallCheckin.getUuid());
            // 将签到记录的状态更新为已被获取
            energyBallService.updateEnergyBallStatusById(userId);

        }else {
            energyCheckinResult.setNewEnergyPoint(BigDecimal.ZERO);
            energyCheckinResult.setNewPower(BigDecimal.ZERO);
            errorCode = ErrorCode.ALREADY_CHECKIN_TODAY;
        }
        return new ResponseEntity
                .Builder<EnergyCheckinResult>()
                .setData(energyCheckinResult)
                .setErrorCode(errorCode)
                .build();
    }
}
