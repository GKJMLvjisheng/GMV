package com.cascv.oas.server.energy.service;

import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.model.EnergyBall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnergyBallService {
    @Autowired
    private EnergyBallMapper energyBallMapper;

    /**
     * 插入energyBall 数据
     * @param energyBall
     * @return
     */
    public int saveEnergyBall(EnergyBall energyBall) {
        int success = energyBallMapper.insertEnergyBall(energyBall);
        return energyBall.getId();
    }

    /**
     * 根据 能量球id 查询能量球信息
     * @param id
     * @return
     */
    public EnergyBall getEnergyBallById(Integer id) {
        return energyBallMapper.selectById(id);
    }

    public int updateEnergyBallStatusById(Integer id) {
        return energyBallMapper.updateEnergyBallStatusById(id);
    }

    /**
     * 获得未被获取过能量的能量球列表
     * @return
     */
    public List<EnergyBall> listEnergyBallByStatus() {
        List<EnergyBall> energyBalls = energyBallMapper.selectByStatus();
        return energyBalls;
    }
}
