package com.cascv.oas.server.energy.controller;


import com.alibaba.fastjson.JSON;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.PageIODomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.EnergyPointBall;
import com.cascv.oas.server.activity.service.ActivityService;
import com.cascv.oas.server.blockchain.wrapper.*;
import com.cascv.oas.server.common.BaseController;
import com.cascv.oas.server.energy.mapper.EnergyWalletTradeRecordMapper;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.*;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.model.ExchangeRateModel;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.news.model.NewsModel;
import com.cascv.oas.server.news.service.NewsService;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.walk.mapper.WalkMapper;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/energyPoint")
@Slf4j
public class EnergyPointController extends BaseController{

  @Autowired
  private ExchangeRateService exchangeRateService;

    @Autowired
    private EnergyService energyService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private EnergyWalletTradeRecordMapper energyWalletTradeRecordMapper;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private WalkMapper walkMapper;
	@Autowired 
	private TimeZoneService timeZoneService;
    @PostMapping(value = "/checkin")
    @ResponseBody
    @Transactional
    /**
     * 签到/返回签到成功即可（奖励用统一接口）
     * @return
     */
    public ResponseEntity<?> checkin() {
//        String userUuid = "USR-0178ea59a6ab11e883290a1411382ce0";
        String userUuid = ShiroUtils.getUserUuid();
        Integer sourceCode = 1;
        if (!energyService.isCheckin(userUuid)) {
//            // today sign in not yet
//            // generate a new Checkin EnergyBall
//            energyService.saveCheckinEnergyBall(userUuid);
//            // insert the Checkin record of this time
//            energyService.saveCheckinEnergyRecord(userUuid);
//            // the result of Checkin
//            energyCheckinResult = energyService.getCheckinEnergy();
//            // add the Checkin point&power in EnergyWallet
//            energyService.updateCheckinEnergyWallet(userUuid);
//            // change the Checkin EnergyBall to Die
//            energyService.updateEnergyBallStatusByUuid(userUuid);
        	//get reward and record
        	activityService.getReward(sourceCode, userUuid);
        	EnergyCheckinResult energyCheckinResult = new EnergyCheckinResult();
        	energyCheckinResult.setNewEnergyPoint(activityService.getNewPoint(sourceCode, 1).getNewPoint());
        	energyCheckinResult.setNewPower(activityService.getNewPower(sourceCode, 2).getNewPower());
        	// change the Checkin EnergyBall to Die
//            activityService.updateEnergyPointBallStatusByUuid(userUuid);
//            activityService.updateEnergyPowerBallStatusByUuid(userUuid);
        	return new ResponseEntity
                    .Builder<EnergyCheckinResult>()
                    .setData(energyCheckinResult)
                    .setErrorCode(ErrorCode.SUCCESS)
                    .build();
        } else {
//            // today sign in yet
//            energyCheckinResult.setNewEnergyPoint(BigDecimal.ZERO);
//            energyCheckinResult.setNewPower(BigDecimal.ZERO);
            return new ResponseEntity
                    .Builder<Integer>()
                    .setData(1)
                    .setErrorCode(ErrorCode.ALREADY_CHECKIN_TODAY)
                    .build();
        }
    }

    @PostMapping(value = "/inquireEnergyPointBall")  //不用power
    @ResponseBody
    public ResponseEntity<?> inquireEnergyPointBall() {
//      String userUuid = "USR-0178ea59a6ab11e883290a1411382ce0";
    	String userUuid = ShiroUtils.getUserUuid();
        EnergyBallResult energyBallResult = energyService.miningEnergyBall(userUuid);
        log.info("energylist={}",JSON.toJSONString(energyBallResult));
        return new ResponseEntity
                .Builder<EnergyBallResult>()
                .setData(energyBallResult)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
    }

    @PostMapping(value = "/takeEnergyPointBall")//不用power
    @ResponseBody
    @Transactional
    public ResponseEntity<?> takeEnergyPointBall(@RequestBody EnergyBallTokenRequest energyBallTokenRequest) {
//        String userUuid = "USR-0178ea59a6ab11e883290a1411382ce0";
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
    	String userUuid = ShiroUtils.getUserUuid();
    	Integer status = 0;
    	String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
    	BigDecimal point = BigDecimal.ZERO;
    	List<EnergyPointBall> energyPointBallList = activityMapper.selectAllByUserUuid(userUuid, status, now);
    	for(int i=0; i<energyPointBallList.size(); i++) {
    		point = point.add(energyPointBallList.get(i).getPoint());
    	}
    	BigDecimal stepNum = walkMapper.selectTodayWalkBall(userUuid, now).getStepNum();
    	
    	
    	
        List<EnergyPointCategory> energyPointCategoryList = new ArrayList<>();

        String[] nameArray = {"手机", "计步", "手表", "家电"};
        Integer[] valueArray = {point.intValue(), stepNum.intValue(), 0, 0};
        Integer[] maxValueArray = {10000, 10000, 20000, 20000};

        for (Integer i = 0; i < 4; i++) {
            EnergyPointCategory energyPointCategory = new EnergyPointCategory();
            energyPointCategory.setId(i);
            energyPointCategory.setName(nameArray[i]);
            energyPointCategory.setValue(valueArray[i]);
            energyPointCategory.setMaxValue(maxValueArray[i]);
            energyPointCategoryList.add(energyPointCategory);
        }
        log.info(JSON.toJSONString(energyPointCategoryList));
        return new ResponseEntity.Builder<List<EnergyPointCategory>>()
                .setData(energyPointCategoryList)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
    }

//    @PostMapping(value = "/inquireNews")
//    @ResponseBody
//    public ResponseEntity<?> inquireNews(PageDomain<Integer> pageInfo) {// here don't use RequestBody  
//      Integer pageSize=pageInfo.getPageSize();
//      Integer pageNum = pageInfo.getPageNum();
//      Integer limit = 3,offset=0;
//      Integer total = newsService.countTotal();
//      if (pageSize != null && pageSize > 0)
//        limit = pageSize;
//      else
//    	limit=total;
//      if (pageNum != null && pageNum > 1)
//        offset = (pageNum - 1) * limit;
//      else
//    	offset=0;
//      
//      List<NewsModel> newsModelList=newsService.selectPage(offset, limit);
//      log.info("pageSize {} total size {}", pageSize, total);
// 
//      List<EnergyNews> energyNewsList = new ArrayList<>();
//      
//      for (NewsModel newsModel : newsModelList){
//        EnergyNews energyNews = new EnergyNews();
//        energyNews.setId(newsModel.getNewsId());
//        energyNews.setTitle(newsModel.getNewsTitle());
//        energyNews.setSummary(newsModel.getNewsAbstract());
//
//        energyNews.setImageLink(newsModel.getNewsPicturePath());
//        log.info(energyNews.getImageLink());
//        energyNews.setNewsLink(newsModel.getNewsUrl());            
//        energyNewsList.add(energyNews);
//      }
//      PageDomain<EnergyNews> pageEnergyNews = new PageDomain<>();
//      pageEnergyNews.setTotal(total);
//      pageEnergyNews.setAsc("desc");
//      pageEnergyNews.setOffset(offset);
//      pageEnergyNews.setPageNum(pageNum);
//      pageEnergyNews.setPageSize(pageSize);
//      pageEnergyNews.setRows(energyNewsList);
//
//      return new ResponseEntity.Builder<PageDomain<EnergyNews>>()
//               .setData(pageEnergyNews)
//               .setErrorCode(ErrorCode.SUCCESS)
//               .build();
//    }
    
@PostMapping(value = "/inquireNews")
@ResponseBody
public ResponseEntity<?> inquireNews(PageDomain<Integer> pageInfo){
     Map<String,Object> info=new HashMap<>();
     Integer pageSize=pageInfo.getPageSize();//获取pageSize
     Integer pageNum = pageInfo.getPageNum();//获取pageNum
     String msg="";
     Integer limit = 3,offset=0,listCount=0,maxPageNum=0;
     
     //新闻总条数
     Integer total = newsService.countTotal();
     
     //有无参数传入
     if (pageSize != null && pageSize > 0)
     {
    	 limit = pageSize;
     }
     else
     {
    	 limit=3;//默认每页3条新闻若无参数传入
     }
     
     if (pageNum != null && pageNum > 1)
     offset = (pageNum - 1) * limit;
     else
     offset=0;
     
     if(total%limit==0)
       maxPageNum=total/limit;
     else
       maxPageNum=(total/limit)+1;
     
     List<NewsModel> newsModelList=newsService.selectPage(offset, limit);
     log.info("pageNum {} limit size {}", pageNum, limit);
     List<EnergyNews> energyNewsList = new ArrayList<>();
 
     for (NewsModel newsModel : newsModelList){
     EnergyNews energyNews = new EnergyNews();
     energyNews.setId(newsModel.getNewsId());
     energyNews.setTitle(newsModel.getNewsTitle());
     energyNews.setSummary(newsModel.getNewsAbstract());
     energyNews.setImageLink(newsModel.getNewsPicturePath());
     log.info(energyNews.getImageLink());
     energyNews.setNewsLink(newsModel.getNewsUrl());            
     energyNewsList.add(energyNews);
     }
     
     PageDomain<EnergyNews> pageEnergyNews = new PageDomain<>();
     pageEnergyNews.setTotal(total);
     pageEnergyNews.setAsc("desc");
     pageEnergyNews.setOffset(offset);
     pageEnergyNews.setPageNum(pageNum);
     pageEnergyNews.setPageSize(pageSize);
     pageEnergyNews.setRows(energyNewsList);
     
     listCount=newsModelList.size();
     if(listCount>0&&pageNum<maxPageNum) {
        info.put("data", pageEnergyNews);
        log.info("***success***");
        msg="数据正常传输";
        info.put("msg", msg);
        return new ResponseEntity.Builder<Map<String,Object>>()
                .setData(info)
                .setErrorCode(ErrorCode.SUCCESS)
                .build();
     }else
     {
       msg="无更多数据";
       info.put("data", pageEnergyNews);
       info.put("msg", msg);
       log.info("no more news in mysql");
       log.info("data传出 msg = {}",msg);
       return new ResponseEntity.Builder<Map<String,Object>>()
               .setData(info)
               .setErrorCode(ErrorCode.SUCCESS)
               .build();
     }
    }
    
    @PostMapping(value = "/inquireCurrentPeriodEnergyPoint")  //要改
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

    @PostMapping(value = "/inquireEnergyPointDetail")//要改
    @ResponseBody
    public ResponseEntity<?> inquireEnergyPointDetail(@RequestBody PageIODomain<Integer> pageInfo) {
        Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer inOrOut=pageInfo.getInOrOut();
        Integer limit = pageSize;
        Integer offset;
 
        if (limit == null) {
          limit = 10;
        }
        
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
 
        List<EnergyChangeDetail> energyPointDetailList = energyService.searchEnergyChange(ShiroUtils.getUserUuid(), offset, limit, inOrOut);
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
    @RequiresPermissions("积分兑换代币")
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
    
    /**
     * @author Ming Yang
     * @return 能量钱包交易明细接口
     */
	@PostMapping(value="/inqureEnergyWalletTradeRecord")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> inqureEnergyWalletTradeRecord(@RequestBody PageDomain<Integer> pageInfo){
		Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer limit = pageSize;
        Integer offset;
 
        if (pageSize == 0) {
          limit = 10;
        }
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
    String searchValue=pageInfo.getSearchValue();//搜索支持模糊查询词语
    List<EnergyWalletTradeRecordInfo> energyWalletTradeRecordList=energyWalletTradeRecordMapper.selectAllTradeRecordBySearchValue(offset, limit, searchValue);
  	for (EnergyWalletTradeRecordInfo energyWalletTradeRecordInfo : energyWalletTradeRecordList) {
		String srcFormater="yyyy-MM-dd HH:mm:ss";
		String dstFormater="yyyy-MM-dd HH:mm:ss";
		String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
		String created=DateUtils.string2Timezone(srcFormater, energyWalletTradeRecordInfo.getCreated(), dstFormater, dstTimeZoneId);
		energyWalletTradeRecordInfo.setCreated(created);
		log.info("newCreated={}",created);
	  }
  	PageDomain<EnergyWalletTradeRecordInfo> energyWalletTradeRecordInfo = new PageDomain<>();
 	 Integer count=energyWalletTradeRecordMapper.countByEnergyWalletTradeRecordBySearchValue(searchValue);
 	 	energyWalletTradeRecordInfo.setTotal(count);
	  	energyWalletTradeRecordInfo.setAsc("desc");
	  	energyWalletTradeRecordInfo.setOffset(offset);
	  	energyWalletTradeRecordInfo.setPageNum(pageNum);
	  	energyWalletTradeRecordInfo.setPageSize(pageSize);
	  	energyWalletTradeRecordInfo.setRows(energyWalletTradeRecordList);
 		return new ResponseEntity.Builder<PageDomain<EnergyWalletTradeRecordInfo>>()
 		        .setData(energyWalletTradeRecordInfo)
 		        .setErrorCode(ErrorCode.SUCCESS)
 		        .build();
    }
    
    /**
     * @author Ming Yang
     * @return 能量钱包积分排行接口
     */
    @PostMapping(value="/inqureEnergyWalletBalanceRecord")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> inqureEnergyWalletBalanceRecord(@RequestBody PageDomain<Integer> pageInfo){
    	Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer limit = pageSize;
        Integer offset;
 
        if (pageSize == 0) {
          limit = 10;
        }
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
       String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
       
    	   List<EnergyWalletBalanceRecordInfo> energyWalletBalanceRecordList=energyWalletTradeRecordMapper.selectAllEnergyWalletBalanceRecordBySearchValue(offset, limit, searchValue);
    	   Integer count=energyWalletTradeRecordMapper.countByEnergyWalletBalanceRecordBySearchValue(searchValue);
    	   PageDomain<EnergyWalletBalanceRecordInfo> energyWalletBalanceRecordInfo = new PageDomain<>();
     	    energyWalletBalanceRecordInfo.setTotal(count);
     	    energyWalletBalanceRecordInfo.setAsc("desc");
     	    energyWalletBalanceRecordInfo.setOffset(offset);
     	    energyWalletBalanceRecordInfo.setPageNum(pageNum);
     	    energyWalletBalanceRecordInfo.setPageSize(pageSize);
     	    energyWalletBalanceRecordInfo.setRows(energyWalletBalanceRecordList);
     		return new ResponseEntity.Builder<PageDomain<EnergyWalletBalanceRecordInfo>>()
     		        .setData(energyWalletBalanceRecordInfo)
     		        .setErrorCode(ErrorCode.SUCCESS)
     		        .build();
    }
    /**
     * @author Ming Yang
     * @return 能量钱包积分转入排行接口
     */
    @PostMapping(value="/inqureEnergyWalletInTotalPointTradeRecord")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> inqureEnergyWalletInTotalPointTradeRecord(@RequestBody PageDomain<Integer> pageInfo){
    	
    	Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer limit = pageSize;
        Integer offset;
 
        if (pageSize == 0) {
          limit = 10;
        }
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
        
  	  //获取当月第一天
  	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  	  Calendar c = Calendar.getInstance();
  	  c.add(Calendar.MONTH, 0);
  	  c.set(Calendar.DAY_OF_MONTH,1);
  	  String nowMonthOfFirstDay =format.format(c.getTime());
        log.info("monthOfFirstDay:{}",nowMonthOfFirstDay);
        
        //获取当前年月日
        Date d = new Date();
        String nowDate = format.format(d);
        log.info("nowDate:{}",nowDate);
        
        String startTime=pageInfo.getStartTime();
        String endTime=pageInfo.getEndTime();
  	  if(startTime=="")
  		  startTime=nowMonthOfFirstDay;
  	  if(endTime=="")
  		  endTime=nowDate;
  	  String searchValue=pageInfo.getSearchValue();
  		List<EnergyWalletPointRecordInfo> energyWalletInTotalPointTradeRecordList=energyWalletTradeRecordMapper.selectAllInTotalPointTradeRecordBySearchValue(startTime, endTime, offset, limit, searchValue);
  		Integer count=energyWalletTradeRecordMapper.countByInTotalPointTradeRecordBySearchValue(startTime, endTime, searchValue);
  		PageDomain<EnergyWalletPointRecordInfo> energyWalletInTotalPointRecordInfo = new PageDomain<>();
	  		energyWalletInTotalPointRecordInfo.setTotal(count);
	  	  	energyWalletInTotalPointRecordInfo.setAsc("desc");
	  	  	energyWalletInTotalPointRecordInfo.setOffset(offset);
	  	  	energyWalletInTotalPointRecordInfo.setPageNum(pageNum);
	  	  	energyWalletInTotalPointRecordInfo.setPageSize(pageSize);
	  	  	energyWalletInTotalPointRecordInfo.setRows(energyWalletInTotalPointTradeRecordList);
    		return new ResponseEntity.Builder<PageDomain<EnergyWalletPointRecordInfo>>()
    		        .setData(energyWalletInTotalPointRecordInfo)
    		        .setErrorCode(ErrorCode.SUCCESS)
    		        .build();
    }
    /**
     * @author Ming Yang
     * @return 能量钱包积分转出排行接口
     */
    
    @PostMapping(value="/inqureEnergyWalletOutTotalPointTradeRecord")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> inqureEnergyWalletOutTotalPointTradeRecord(@RequestBody PageDomain<Integer> pageInfo){
  	  
    	Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer limit = pageSize;
        Integer offset;
 
        if (pageSize == 0) {
          limit = 10;
        }
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
    	
  	  //获取当月第一天
  	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  	  Calendar c = Calendar.getInstance();
  	  c.add(Calendar.MONTH, 0);
  	  c.set(Calendar.DAY_OF_MONTH,1);
  	  String nowMonthOfFirstDay =format.format(c.getTime());
        log.info("monthOfFirstDay:{}",nowMonthOfFirstDay);
        
        //获取当前年月日
        Date d = new Date();
        String nowDate = format.format(d);
        log.info("nowDate:{}",nowDate);
  	  
        String startTime=pageInfo.getStartTime();
        String endTime=pageInfo.getEndTime();
  	  if(startTime=="")
  		  startTime=nowMonthOfFirstDay;
  	  if(endTime=="")
  		  endTime=nowDate;
  	  String searchValue=pageInfo.getSearchValue();
		  List<EnergyWalletPointRecordInfo> energyWalletOutTotalPointTradeRecordList=energyWalletTradeRecordMapper.selectAllOutTotalPointTradeRecordBySearchValue(startTime, endTime, offset, limit, searchValue);
	  	  PageDomain<EnergyWalletPointRecordInfo> energyWalletOutTotalPointRecordInfo = new PageDomain<>();
	  	  Integer count=energyWalletTradeRecordMapper.countByOutTotalPointTradeRecordBySearchValue(startTime, endTime, searchValue);
		  	energyWalletOutTotalPointRecordInfo.setTotal(count);
		  	energyWalletOutTotalPointRecordInfo.setAsc("desc");
		  	energyWalletOutTotalPointRecordInfo.setOffset(offset);
		  	energyWalletOutTotalPointRecordInfo.setPageNum(pageNum);
		  	energyWalletOutTotalPointRecordInfo.setPageSize(pageSize);
		  	energyWalletOutTotalPointRecordInfo.setRows(energyWalletOutTotalPointTradeRecordList);
	  		return new ResponseEntity.Builder<PageDomain<EnergyWalletPointRecordInfo>>()
	  		        .setData(energyWalletOutTotalPointRecordInfo)
	  		        .setErrorCode(ErrorCode.SUCCESS)
	  		        .build();
    }
}
