package com.cascv.oas.server.energy.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.EnergyBallResult;
import com.cascv.oas.server.energy.vo.FirstPageReturn;
import com.cascv.oas.server.utils.ShiroUtils;

@RestController
@RequestMapping(value = "/api/v1/energyPoint")
public class FirstPageController {
	
	@Autowired
    private EnergyService energyService;
	
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
        System.out.println(energyPoint);
        if (energyPoint != null) {
        	point = energyPoint.getPoint();
        	System.out.println(point);
        } else {
        	point = BigDecimal.ZERO;
        	System.out.println(point);
        }        
		firstPageReturn.setPower(power);
		System.out.println(firstPageReturn.getPower());
		firstPageReturn.setPoint(point);
		System.out.println(firstPageReturn.getPoint());
//        firstPageReturn.energyBallReturn(energyBallResult);
		return new ResponseEntity.Builder<FirstPageReturn>()
				.setData(firstPageReturn)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
//		return null;
	}

}
