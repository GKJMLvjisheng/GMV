package com.cascv.oas.server.energy.controller;


import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.wrapper.*;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.*;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.model.ExchangeRateModel;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.news.model.NewsModel;
import com.cascv.oas.server.news.service.NewsService;
import com.cascv.oas.server.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/energyPoint")
@Slf4j
public class EnergyPointController {

  @Autowired
  private ExchangeRateService exchangeRateService;

    @Autowired
    private EnergyService energyService;
    @Autowired
    private NewsService newsService;

    @PostMapping(value = "/checkin")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> checkin() {
//        String userUuid = "USR-0178ea59a6ab11e883290a1411382ce0";
        String userUuid = ShiroUtils.getUserUuid();
        EnergyCheckinResult energyCheckinResult = new EnergyCheckinResult();
        ErrorCode errorCode = ErrorCode.SUCCESS;
        if (!energyService.isCheckin(userUuid)) {
            // today sign in not yet
            // generate a new Checkin EnergyBall
            energyService.saveCheckinEnergyBall(userUuid);
            // insert the Checkin record of this time
            energyService.saveCheckinEnergyRecord(userUuid);
            // the result of Checkin
            energyCheckinResult = energyService.getCheckinEnergy();
            // add the Checkin point&power in EnergyWallet
            energyService.updateCheckinEnergyWallet(userUuid);
            // change the Checkin EnergyBall to Die
            energyService.updateEnergyBallStatusByUuid(userUuid);
        } else {
            // today sign in yet
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

    @PostMapping(value = "/inquireEnergyBall")
    @ResponseBody
    public ResponseEntity<?> inquireEnergyBall() {
//        String userUuid = "USR-0178ea59a6ab11e883290a1411382ce0";
    	String userUuid = ShiroUtils.getUserUuid();
        EnergyBallResult energyBallResult = energyService.miningEnergyBall(userUuid);
        return new ResponseEntity
                .Builder<EnergyBallResult>()
                .setData(energyBallResult)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
    }

    @PostMapping(value = "/takeEnergyBall")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> takeEnergyBall(@RequestBody EnergyBallTokenRequest energyBallTokenRequest) {
 //       String userUuid = "USR-0178ea59a6ab11e883290a1411382ce0";
        String userUuid = ShiroUtils.getUserUuid();
        // 挖矿查询
        energyService.miningEnergyBall(userUuid);
        ErrorCode errorCode = ErrorCode.SUCCESS;
        EnergyBallTakenResult energyBallTakenResult = energyService
                .getEnergyBallTakenResult(userUuid, energyBallTokenRequest.getBallId());
        if (energyBallTakenResult == null) {
            errorCode = ErrorCode.GENERAL_ERROR;
        }
        return new ResponseEntity
                .Builder<EnergyBallTakenResult>()
                .setData(energyBallTakenResult)
                .setErrorCode(errorCode)
                .build();
    }

    @PostMapping(value = "/inquirePower")
    @ResponseBody
    public ResponseEntity<?> inquirePower() {
        EnergyWallet energyWallet = energyService.findByUserUuid(ShiroUtils.getUserUuid());
        if (energyWallet != null) {
            return new ResponseEntity.Builder<Integer>()
                    .setData(energyWallet.getPower().intValue())
                    .setErrorCode(ErrorCode.SUCCESS)
                    .build();
        } else {
            return new ResponseEntity.Builder<Integer>()
                    .setData(0)
                    .setErrorCode(ErrorCode.NO_ENERGY_POINT_ACCOUNT)
                    .build();
        }
    }

    @PostMapping(value = "/inquireEnergyPoint")
    @ResponseBody
    public ResponseEntity<?> inquireEnergyPoint() {
        EnergyWallet energyPoint = energyService.findByUserUuid(ShiroUtils.getUserUuid());
        if (energyPoint != null) {
            return new ResponseEntity.Builder<Integer>()
                    .setData(energyPoint.getPoint().intValue())
                    .setErrorCode(ErrorCode.SUCCESS)
                    .build();
        } else {
            return new ResponseEntity.Builder<Integer>()
                    .setData(0)
                    .setErrorCode(ErrorCode.NO_ENERGY_POINT_ACCOUNT)
                    .build();
        }
    }


    @PostMapping(value = "/inquireEnergyPointByCategory")
    @ResponseBody
    public ResponseEntity<?> inquireEnergyPointByCategory(String periodType) {
        List<EnergyPointCategory> energyPointCategoryList = new ArrayList<>();

        String[] nameArray = {"手机", "手表", "家电"};
        Integer[] valueArray = {10000, 5000, 0};
        Integer[] maxValueArray = {20000, 20000, 20000};

        for (Integer i = 0; i < 3; i++) {
            EnergyPointCategory energyPointCategory = new EnergyPointCategory();
            energyPointCategory.setId(i);
            energyPointCategory.setName(nameArray[i]);
            energyPointCategory.setValue(valueArray[i]);
            energyPointCategory.setMaxValue(maxValueArray[i]);
            energyPointCategoryList.add(energyPointCategory);
        }
        return new ResponseEntity.Builder<List<EnergyPointCategory>>()
                .setData(energyPointCategoryList)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
    }

    @PostMapping(value = "/inquireNews")
    @ResponseBody
    public ResponseEntity<?> inquireNews(@RequestBody PageDomain<Integer> pageInfo) {
        List<NewsModel> list=newsService.selectAllNews();
        int length=list.size();
        //默认一页显示3条
        int pageSize=0;
        try{
        	pageSize=(pageInfo.getPageSize()>0&&pageInfo.getPageSize()<=length)?pageInfo.getPageSize():length;
        	}
        catch(NullPointerException e){
        	pageSize=length;
            log.info(e.getMessage());
        }
        //总共的页数
        int pageTotalNum=0;
        if(length/pageSize>0) {
        	if(length%pageSize!=0) {
        		pageTotalNum=length/pageSize+1;
        	}
        	else {
        		pageTotalNum=length/pageSize;
        	}
        }else {
        	pageTotalNum=1;
        }
        //入参为第几页(pageNum>=1,若pageNum=0或者不存在,则报错)
        int pageNum;       
        try {
        	if(pageInfo.getPageNum()>0&&pageInfo.getPageNum()<=pageTotalNum){

        pageNum=pageInfo.getPageNum();
        //每页从第几条开始(offset>=0)
        int offset=(pageNum-1)*pageSize;
        //所在页数的数据量
        int pageEnd=(pageNum<pageTotalNum)?(offset+pageSize):length;          
           List<EnergyNews> energyNewsList = new ArrayList<>();
          for (int i=offset; i<pageEnd; i++){
               EnergyNews energyNews = new EnergyNews();
               energyNews.setId(list.get(i).getNewsId());
               energyNews.setTitle(list.get(i).getNewsTitle());
               energyNews.setSummary(list.get(i).getNewsTitle());
               energyNews.setImageLink(list.get(i).getNewsPicturePath());
               energyNews.setNewsLink(list.get(i).getNewsUrl());            
               energyNewsList.add(energyNews);
           }

           PageDomain<EnergyNews> pageEnergyNews = new PageDomain<>();
           pageEnergyNews.setTotal(length);
           pageEnergyNews.setAsc("asc");
           pageEnergyNews.setOffset(offset);
           pageEnergyNews.setPageNum(pageNum);
           pageEnergyNews.setPageSize(pageSize);
           pageEnergyNews.setRows(energyNewsList);

           return new ResponseEntity.Builder<PageDomain<EnergyNews>>()
                   .setData(pageEnergyNews)
                   .setErrorCode(ErrorCode.SUCCESS)
                   .build();
        	}
        else { 
        	return new ResponseEntity.Builder<Integer>()
                    .setData(1)
                    .setErrorCode(ErrorCode.GENERAL_ERROR)
                    .build();
             }
         }
        catch(NullPointerException e){
        	return new ResponseEntity.Builder<Integer>()
                    .setData(1)
                    .setErrorCode(ErrorCode.GENERAL_ERROR)
                    .build();
        }
    }
    
    @PostMapping(value = "/inquireCurrentPeriodEnergyPoint")
    @ResponseBody
    public ResponseEntity<?> inquireCurrentPeriodEnergyPoint() {
        //produce and cosume during current peroid
        CurrentPeriodEnergyPoint currentPeriodEnergyPoint = new CurrentPeriodEnergyPoint();
        String today = DateUtils.dateTimeNow(DateUtils.YYYY_MM);
        BigDecimal consumed = energyService.summaryOutPoint(ShiroUtils.getUserUuid(), today);
        BigDecimal produced = energyService.summaryInPoint(ShiroUtils.getUserUuid(), today);
        if (consumed == null) {
          consumed = BigDecimal.ZERO;
        }
        if (produced == null) {
          produced = BigDecimal.ZERO;
        }
        currentPeriodEnergyPoint.setConsumedEnergyPoint(consumed.intValue());
        currentPeriodEnergyPoint.setProducedEnergyPoint(produced.intValue());

        return new ResponseEntity.Builder<CurrentPeriodEnergyPoint>()
                .setData(currentPeriodEnergyPoint)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
    }

    @PostMapping(value = "/inquireEnergyPointDetail")
    @ResponseBody
    public ResponseEntity<?> inquireEnergyPointDetail(@RequestBody PageDomain<Integer> pageInfo) {
        Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer limit = pageSize;
        Integer offset;
 
        if (limit == null) {
          limit = 10;
        }
        
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
 
        List<EnergyChangeDetail> energyPointDetailList = energyService.searchEnergyChange(ShiroUtils.getUserUuid(), offset, limit);
        Integer count = energyService.countEnergyChange(ShiroUtils.getUserUuid());
        
        PageDomain<EnergyChangeDetail> pageEnergyPointDetail = new PageDomain<>();
        pageEnergyPointDetail.setTotal(count);
        pageEnergyPointDetail.setAsc("asc");
        pageEnergyPointDetail.setOffset(offset);
        pageEnergyPointDetail.setPageNum(pageNum);
        pageEnergyPointDetail.setPageSize(pageSize);
        pageEnergyPointDetail.setRows(energyPointDetailList);

        return new ResponseEntity.Builder<PageDomain<EnergyChangeDetail>>()
                .setData(pageEnergyPointDetail)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
    }


    @PostMapping(value = "/redeemPoint")
    @ResponseBody
    public ResponseEntity<?> redeemPoint(@RequestBody EnergyPointRedeem energyPointRedeem) {
      
      if (energyPointRedeem.getPeriod() == null) {
        return new ResponseEntity.Builder<Integer>()
            .setData(0)
            .setErrorCode(ErrorCode.NO_DATE_SPECIFIED)
            .build();
      }
      ExchangeRateModel exchangeRateModel = exchangeRateService.getRate(energyPointRedeem.getPeriod(), CurrencyCode.POINT);
      if (exchangeRateModel == null) {
        return new ResponseEntity.Builder<Integer>()
            .setData(0)
            .setErrorCode(ErrorCode.NO_AVAILABLE_EXCHANGE_RATE)
            .build();
      }
      
      BigDecimal rate = BigDecimal.ONE.divide(exchangeRateModel.getRate());
      BigDecimal userRate = energyPointRedeem.getRate();
      if (userRate != null && userRate.compareTo(BigDecimal.ZERO) != 0 && userRate.compareTo(rate) > 0){
        return new ResponseEntity.Builder<Integer>()
            .setData(0)
            .setErrorCode(ErrorCode.RATE_NOT_ACCEPTABLE)
            .build();
      } 
      ErrorCode errorCode = energyService.redeem(ShiroUtils.getUserUuid(), energyPointRedeem.getPeriod());
      return new ResponseEntity.Builder<Integer>()
              .setData(0)
              .setErrorCode(errorCode)
              .build();
    }

    @PostMapping(value = "/inquirePointFactor")
    @ResponseBody
    public ResponseEntity<?> inquireEnergyPointFactor(@RequestBody EnergyPointFactorRequest energyPointFactorRequest) {
      EnergyPointFactor energyPointFactor = new EnergyPointFactor();  
      String date = energyPointFactorRequest.getDate();
      if (date == null) {
        return new ResponseEntity.Builder<EnergyPointFactor>()
            .setData(energyPointFactor)
            .setErrorCode(ErrorCode.NO_DATE_SPECIFIED)
            .build();
      }
      energyPointFactor.setDate(date);
      ExchangeRateModel exchangeRateModel = exchangeRateService.getRate(date, CurrencyCode.POINT);
      if (exchangeRateModel == null) {
        return new ResponseEntity.Builder<EnergyPointFactor>()
            .setData(energyPointFactor)
            .setErrorCode(ErrorCode.NO_AVAILABLE_EXCHANGE_RATE)
            .build();
      }
      energyPointFactor.setFactor(BigDecimal.ONE.divide(exchangeRateModel.getRate()).doubleValue());
        
      BigDecimal amount = energyService.summaryPoint(ShiroUtils.getUserUuid(), date);
      if (amount == null)
       	amount=BigDecimal.ZERO;
      energyPointFactor.setAmount(amount);
      return new ResponseEntity.Builder<EnergyPointFactor>()
                .setData(energyPointFactor)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
    }
   
}
