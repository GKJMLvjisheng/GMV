package com.cascv.oas.server.blockchain.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.model.EnergyBall;
import com.cascv.oas.server.blockchain.model.EnergyPoint;
import com.cascv.oas.server.blockchain.model.EnergyPointDetail;
import com.cascv.oas.server.blockchain.service.EnergyPointService;
import com.cascv.oas.server.blockchain.vo.EnergyPointCheckinResult;
import com.cascv.oas.server.blockchain.vo.EnergyPointRedeem;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.blockchain.vo.CurrentPeriodEnergyPoint;
import com.cascv.oas.server.blockchain.vo.EnergyBallResult;
import com.cascv.oas.server.blockchain.vo.EnergyBallTakenResult;
import com.cascv.oas.server.blockchain.vo.EnergyNews;
import com.cascv.oas.server.blockchain.vo.EnergyPointCategory;
import com.cascv.oas.server.blockchain.vo.EnergyPointFactor;
@RestController
@RequestMapping(value="/api/v1/energyPoint")
public class EnergyPointController {
  
  @Autowired
  private EnergyPointService energyPointService;
	
  @PostMapping(value="/checkin")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> checkin(){
    EnergyPointCheckinResult energyPointCheckinResult = new EnergyPointCheckinResult();
    energyPointCheckinResult.setNewEnergyPoint(15);
    energyPointCheckinResult.setNewPower(10);
    return new ResponseEntity.Builder<EnergyPointCheckinResult>().setData(energyPointCheckinResult).setErrorCode(ErrorCode.SUCCESS).build();
  }
  
  
  @PostMapping(value="/inquireEnergyBall")
  @ResponseBody
  public ResponseEntity<?> inquireEnergyBall(){
    EnergyBallResult energyBallResult = new EnergyBallResult();
    List<EnergyBall> energyBallList = new ArrayList<>();
    for (Integer i = 0; i < 16; i++) {
      EnergyBall energyBall = new EnergyBall();
      energyBall.setUuid(String.valueOf(i+1));
      energyBall.setType(1);
      energyBall.setValue(i + 6);
      energyBall.setName("daily");
      energyBall.setStartDate(DateUtils.getDate());
      energyBall.setEndDate(DateUtils.getDate());
      energyBallList.add(energyBall);
      
    }
    energyBallResult.setEnergyBallList(energyBallList);
    energyBallResult.setOngoingEnergySummary(236);
    return new ResponseEntity.Builder<EnergyBallResult>().setData(energyBallResult).setErrorCode(ErrorCode.SUCCESS).build();
  }
  
  @PostMapping(value="/takeEnergyBall")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> takeEnergyBall(Integer ballId){
    EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
    energyBallTakenResult.setNewEnergyPonit(15);
    energyBallTakenResult.setNewPower(0);
    return new ResponseEntity.Builder<EnergyBallTakenResult>().setData(energyBallTakenResult).setErrorCode(ErrorCode.SUCCESS).build();
  }
  
  @PostMapping(value="/inquirePower")
  @ResponseBody
  public ResponseEntity<?> inquirePower(){
	EnergyPoint energyPoint = energyPointService.findByUserUuid(ShiroUtils.getUserUuid());
	if (energyPoint != null) {
		return new ResponseEntity.Builder<Integer>()
				.setData(energyPoint.getPower())
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	} else { 
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.NO_ENERGY_POINT_ACCOUNT)
				.build();
	}
  }
  
  @PostMapping(value="/inquireEnergyPoint")
  @ResponseBody
  public ResponseEntity<?> inquireEnergyPoint(){
    EnergyPoint energyPoint = energyPointService.findByUserUuid(ShiroUtils.getUserUuid());
	if (energyPoint != null) {
		return new ResponseEntity.Builder<Integer>()
				.setData(energyPoint.getBalance())
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	} else { 
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.NO_ENERGY_POINT_ACCOUNT)
				.build();
	}
  }
  
  
  @PostMapping(value="/inquireEnergyPointByCategory")
  @ResponseBody
  public ResponseEntity<?> inquireEnergyPointByCategory(String periodType) {
    List<EnergyPointCategory> energyPointCategoryList = new ArrayList<>();
    
    String [] nameArray = {"手机","手表","家电"};
    Integer [] valueArray = {10000,5000,0};
    
    for (Integer i = 0; i < 3; i++) {
      EnergyPointCategory energyPointCategory = new EnergyPointCategory();
      energyPointCategory.setId(i);
      energyPointCategory.setName(nameArray[i]);
      energyPointCategory.setValue(valueArray[i]);
      energyPointCategoryList.add(energyPointCategory);
    }
    return new ResponseEntity.Builder<List<EnergyPointCategory>>()
        .setData(energyPointCategoryList)
        .setErrorCode(ErrorCode.SUCCESS)
        .build();
  }
  
  @PostMapping(value="/inquireNews")
  @ResponseBody
  public ResponseEntity<?> inquireNews(PageDomain<Integer> pageInfo) {
    
    String [] titleArray = {"A", "B", "C"};
    String [] summaryArray = {"中国人民大学宿舍起火 目击者：楼顶几乎烧没了", "国内核心互联网软件无一幸免", "你的微信或已被操控"};
    String [] newsArray = {
          "http://news.sina.com.cn/gov/xlxw/2018-08-21/doc-ihhzsnea3430780.shtml",
          "http://news.sina.com.cn/o/2018-08-21/doc-ihhzsnea4009465.shtml",
          "http://news.sina.com.cn/c/2018-08-21/doc-ihhzsnea0935440.shtml"
    };
        
    List<EnergyNews> energyNewsList = new ArrayList<> ();   
    for (Integer i = 0 ; i < 3; i++) {
      EnergyNews energyNews = new EnergyNews();
      energyNews.setId(i+1);
      energyNews.setTitle(titleArray[i]);
      energyNews.setSummary(summaryArray[i]);
      energyNews.setImageLink("/img/"+String.valueOf(i+1)+".jpg");
      energyNews.setNewsLink(newsArray[i]);
      energyNewsList.add(energyNews);
    }
    PageDomain<EnergyNews> pageEnergyNews= new PageDomain<>();
    pageEnergyNews.setTotal(3);
    pageEnergyNews.setAsc("asc");
    pageEnergyNews.setOffset(0);
    pageEnergyNews.setPageNum(1);
    pageEnergyNews.setPageSize(3);
    pageEnergyNews.setRows(energyNewsList);
    
    return new ResponseEntity.Builder<PageDomain<EnergyNews>>()
        .setData(pageEnergyNews)
        .setErrorCode(ErrorCode.SUCCESS)
        .build();
  }

  @PostMapping(value="/inquireCurrentPeriodEnergyPoint")
  @ResponseBody
  public ResponseEntity<?> inquireCurrentPeriodEnergyPoint(){
    //produce and cosume during current peroid
    CurrentPeriodEnergyPoint currentPeriodEnergyPoint = new CurrentPeriodEnergyPoint();
    currentPeriodEnergyPoint.setConsumedEnergyPoint(3450);
    currentPeriodEnergyPoint.setProducedEnergyPoint(5643);

    return new ResponseEntity.Builder<CurrentPeriodEnergyPoint>()
      .setData(currentPeriodEnergyPoint)
      .setErrorCode(ErrorCode.SUCCESS)
      .build();
  }

  @PostMapping(value="/inquireEnergyPointDetail")
  @ResponseBody
  public ResponseEntity<?> inquireEnergyPointDetail(@RequestBody PageDomain<Integer> pageInfo){
    
    List<EnergyPointDetail> energyPointDetailList = new ArrayList<>();
    
    Calendar calendar = new GregorianCalendar();
    Date now = new Date();
    calendar.setTime(now);

    for (Integer i = 0; i< 3; i++) {
      EnergyPointDetail energyPointDetail = new EnergyPointDetail();
      energyPointDetail.setActivity("");
      energyPointDetail.setCategory("");
      
      calendar.add(Calendar.DATE, 1+i);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

      String str = formatter.format(calendar.getTime());
      energyPointDetail.setCreated(str);
      energyPointDetail.setSource("手机");
      energyPointDetail.setUuid(ShiroUtils.getUserUuid());
      energyPointDetail.setUuid(String.valueOf(i+3));
      energyPointDetail.setValue(i*5+5);
      energyPointDetailList.add(energyPointDetail);
    }
    PageDomain<EnergyPointDetail> pageEnergyPointDetail= new PageDomain<>();
    pageEnergyPointDetail.setTotal(3);
    pageEnergyPointDetail.setAsc("asc");
    pageEnergyPointDetail.setOffset(0);
    pageEnergyPointDetail.setPageNum(1);
    pageEnergyPointDetail.setPageSize(3);
    pageEnergyPointDetail.setRows(energyPointDetailList);

    return new ResponseEntity.Builder<PageDomain<EnergyPointDetail>>()
      .setData(pageEnergyPointDetail)
      .setErrorCode(ErrorCode.SUCCESS)
      .build();
  }


  @PostMapping(value="/redeemPoint")
  @ResponseBody
  public ResponseEntity<?> redeemPoint(@RequestBody EnergyPointRedeem energyPointRedeem){
    return new ResponseEntity.Builder<Integer>()
      .setData(0)
      .setErrorCode(ErrorCode.SUCCESS)
      .build();
  }

  
  @PostMapping(value="/inquirePointFactor")
  @ResponseBody
  public ResponseEntity<?> inquireEnergyPointFactor(String date){

    EnergyPointFactor energyPointFactor = new EnergyPointFactor();
    energyPointFactor.setFactor(0.00001);
    energyPointFactor.setDate(date);
    energyPointFactor.setAmount(156);
    return new ResponseEntity.Builder<EnergyPointFactor>()
      .setData(energyPointFactor)
      .setErrorCode(ErrorCode.SUCCESS)
      .build();
  }
      
}
