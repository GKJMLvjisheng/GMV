package com.cascv.oas.server.energy.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.mapper.EnergySourcePointMapper;
import com.cascv.oas.server.energy.model.EnergySourcePoint;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.EnergyBallResult;
import com.cascv.oas.server.energy.vo.EnergyBallWrapper;
import com.cascv.oas.server.energy.vo.FirstPageReturn;
import com.cascv.oas.server.utils.ShiroUtils;

@RestController
@RequestMapping(value = "/api/v1/energyPoint")
public class FirstPageController {
	
	@Autowired
    private EnergyService energyService;
	private EnergySourcePointMapper energySourcePointMapper;
	private EnergyBallMapper energyBallMapper;
	
	private static final Integer STATUS_OF_ACTIVE_ENERGYBALL = 1;       // 能量球活跃状态，可被获取
    private static final Integer SOURCE_CODE_OF_MINING = 2;             // 能量球来源：挖矿为2
	
	@PostMapping(value = "/firstPageReturn")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> firstPageReturn(){
		String userUuid = ShiroUtils.getUserUuid();
		System.out.println(userUuid);
		FirstPageReturn firstPageReturn = new FirstPageReturn();
		BigDecimal power;
        BigDecimal point;
		EnergyBallResult energyBallResult = energyService.miningEnergyBall(userUuid);
		System.out.println(energyBallResult);
		EnergyWallet energyPower = energyService.findByUserUuid(userUuid);
		System.out.println(energyPower);
		if (energyPower != null) {
			power = energyPower.getPower();
			System.out.println(power);
        } else {
        	power = BigDecimal.ZERO;
        	System.out.println(power);
        }
        EnergyWallet energyPoint = energyService.findByUserUuid(userUuid);
        if (energyPoint != null) {
        	point = energyPoint.getPoint();
        } else {
        	point = BigDecimal.ZERO;
        }
        EnergySourcePoint energySourcePoint = energySourcePointMapper.queryBySourceCode(SOURCE_CODE_OF_MINING);
        System.out.println(energySourcePoint);
        BigDecimal ongoingEnergySummary = energyService.miningGenerator(userUuid, energySourcePoint);
        System.out.println(ongoingEnergySummary);
        BigDecimal pointIncreaseSpeed = energySourcePoint.getIncreaseSpeed();            // 挖矿球增长速度
        BigDecimal pointCapacityEachBall = energySourcePoint.getMaxValue();      // 挖矿球最大容量
        BigDecimal timeGap = pointCapacityEachBall.divide(pointIncreaseSpeed,
                0, BigDecimal.ROUND_HALF_UP);// 能量球起始时间和结束时间之差
        List<EnergyBallWrapper> energyBallWrappers = energyBallMapper
                .selectPartByPointSourceCode(userUuid,
                        SOURCE_CODE_OF_MINING,
                        STATUS_OF_ACTIVE_ENERGYBALL,
                        timeGap.intValue());
		firstPageReturn.setPower(power);
		firstPageReturn.setPoint(point);
		firstPageReturn.setEnergyBallList(energyBallWrappers);
		firstPageReturn.setOngoingEnergySummary(ongoingEnergySummary);
//        firstPageReturn.energyBallReturn(energyBallResult);
		return new ResponseEntity.Builder<FirstPageReturn>()
				.setData(firstPageReturn)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
//		return null;
	}

}
