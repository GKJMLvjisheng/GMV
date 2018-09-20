package com.cascv.oas.server.energy.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.mapper.EnergySourcePointMapper;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.mapper.EnergyTradeRecordMapper;
import com.cascv.oas.server.energy.mapper.EnergyWalletMapper;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import com.cascv.oas.server.energy.vo.ActivityResult;
import com.cascv.oas.server.energy.vo.ActivityResultList;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import com.cascv.oas.server.energy.vo.EnergyOfficialAccountResult;
import com.cascv.oas.server.energy.vo.EnergyPowerChangeDetail;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PowerService {
	@Autowired
    private EnergyBallMapper energyBallMapper;
    @Autowired
    private EnergyTradeRecordMapper energyTradeRecordMapper;
    @Autowired
    private EnergyWalletMapper energyWalletMapper;
    @Autowired
    private EnergySourcePowerMapper energySourcePowerMapper;
    
    private EnergyBall oaEnergyBall = new EnergyBall();   
    private static final Integer STATUS_OF_ACTIVE_ENERGYBALL = 1;       // 能量球活跃状态，可被获取
//    private static final Integer STATUS_OF_DIE_ENERGYBALL = 0;          // 能量球死亡状态，不可被获取
//    private static final Integer STATUS_OF_ACTIVE_ENERGYRECORD = 1;    // 能量记录活跃状态，可被获取
    private static final Integer STATUS_OF_DIE_ENERGYRECORD = 0;       // 能量记录活跃状态，不可被获取
    private static final Integer POWER_SOURCE_CODE_OF_OFFICIALACCOUNT = 3;            // 算力提升来源：关注公众号为3   
    private static final Integer POINT_SOURCE_CODE_OF_OFFICIALACCOUNT = 0;            // 与积分相关的都为0
    private static final Integer ENEGY_IN = 1;               // 能量增加为1，能量减少为0
    
    
    /**
     * 判断是否已经备份过钱包，已经备份过返回1，没有备份返回0
     * @return
     */
    public Integer isBackupsWallet(String userUuid) {
    	
    	ActivityResult activityStatus = energySourcePowerMapper.selectStatusByUserUuid(userUuid);
    	Integer status = activityStatus.getStatus();
		return status;
    	
    }
    
    
    /**
     * 查询各活动的完成情况表
     * @return
     */
    
    public List<ActivityResult> searchActivityStatus(String userUuid){
    	List<ActivityResult> activityResultList = energySourcePowerMapper.selectByUserUuid(userUuid);
    	List<ActivityResult> statusList = new ArrayList<>();
    	for(ActivityResult activityResult : activityResultList) {
    		statusList.add(activityResult);
    	}
		return statusList;    	
    }
    
    /**
     * 
      * 插入关注公众号获取到的oaEnergyBall
     * @param userUuid
     * @return
     */
    public int saveOAEnergyBall(String userUuid,String now){    	       
    	//String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
    	oaEnergyBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        oaEnergyBall.setUserUuid(userUuid);
        oaEnergyBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
        oaEnergyBall.setPoint(this.getOAEnergy().getNewEnergyPoint());
        oaEnergyBall.setPower(this.getOAEnergy().getNewPower());
        log.info(this.getOAEnergy().getNewPower().toString());
        oaEnergyBall.setPointSource(POINT_SOURCE_CODE_OF_OFFICIALACCOUNT);
        oaEnergyBall.setPowerSource(POWER_SOURCE_CODE_OF_OFFICIALACCOUNT);
        oaEnergyBall.setTimeCreated(now);
        oaEnergyBall.setTimeUpdated(now);
        return energyBallMapper.insertEnergyBall(oaEnergyBall);
    }
    /**
     * 
     * 插入算力提升的记录
     * @param userUuid
     * @return
     */
    public int saveOAEnergyRecord(String userUuid,String now){
    	this.saveOAEnergyBall(userUuid,now);
        EnergyTradeRecord energyTradeRecord = new EnergyTradeRecord();
        energyTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
        energyTradeRecord.setUserUuid(userUuid);
        energyTradeRecord.setEnergyBallUuid(oaEnergyBall.getUuid());
        energyTradeRecord.setInOrOut(ENEGY_IN);
        energyTradeRecord.setTimeCreated(now);
        energyTradeRecord.setTimeUpdated(now);
        energyTradeRecord.setStatus(STATUS_OF_DIE_ENERGYRECORD);
        energyTradeRecord.setPointChange(this.getOAEnergy().getNewEnergyPoint());
        energyTradeRecord.setPowerChange(this.getOAEnergy().getNewPower());
        return energyTradeRecordMapper.insertEnergyTradeRecord(energyTradeRecord);
    }

    /**
     * 
     * 在能量钱包(EnergyWallet)中添加记录
     * @param
     * @param userUuid
     * @return
     */
    public void updateOAEnergyWallet(String userUuid){
        String uuid = energyWalletMapper.selectByUserUuid(userUuid).getUuid();
        energyWalletMapper.increasePoint(uuid, this.getOAEnergy().getNewEnergyPoint());
        energyWalletMapper.increasePower(uuid, this.getOAEnergy().getNewPower());
    }
    /**
     * 关注公众号获取到的属性：增加积分、算力的数值
     * @return
     */
    public EnergyOfficialAccountResult getOAEnergy() {
        BigDecimal point = BigDecimal.ZERO;
        BigDecimal power = energySourcePowerMapper.queryPowerSingle(POWER_SOURCE_CODE_OF_OFFICIALACCOUNT);
        EnergyOfficialAccountResult energyOAResult = new EnergyOfficialAccountResult();
        energyOAResult.setNewEnergyPoint(point);
        energyOAResult.setNewPower(power);
        return energyOAResult;
    }
    
    /**
     * 查询算力详情
     * @return
     */
    public List<EnergyPowerChangeDetail> searchEnergyPowerChange(String userUuid, Integer offset, Integer limit){
    	
    	List<EnergyPowerChangeDetail> energyPowerChangeDetailList = energyTradeRecordMapper.selectPowerByPage(userUuid, offset, limit);
    	List<EnergyPowerChangeDetail> powerList = new ArrayList<>();
    	for(EnergyPowerChangeDetail energyPowerChangeDetail : energyPowerChangeDetailList) {
    		energyPowerChangeDetail.setValue(energyPowerChangeDetail.getPowerChange().intValue());
    		
    		if(energyPowerChangeDetail.getValue() != 0) {
    			powerList.add(energyPowerChangeDetail);
    		}
    	}
    	
		return powerList;
    	
    }
}
