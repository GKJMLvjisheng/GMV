package com.cascv.oas.server.exchange.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.HttpUtils;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.mapper.ExchangeRateMapper;
import com.cascv.oas.server.exchange.model.ExchangeRateModel;

import net.sf.json.JSONObject;

@Service
public class ExchangeRateService {
   
  @Autowired
  private ExchangeRateMapper exchangeRateMapper;
  
  @Value("${exchangeHourseuUrl}")
  private String exchangeHourseUrl;

  
  private Integer insertRate(String time, CurrencyCode currency, BigDecimal rate, String comment) {
    String now = DateUtils.getTime();
    ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
    exchangeRateModel.setComment(comment);
    exchangeRateModel.setCreated(now);
    exchangeRateModel.setUpdated(now);
    exchangeRateModel.setCurrency(currency.getCode());
    exchangeRateModel.setTime(time);
    exchangeRateModel.setRate(rate);
    return exchangeRateMapper.insertSelective(exchangeRateModel);
  }
  /**
   * 从交易所获取各个币值相对OAS币的rate，插入数据库
   * @param comment
   */
  public void getRatesFromNet(String comment) {
	 String result =  HttpUtils.sendGet(exchangeHourseUrl.concat("/").concat(Long.toString(new Date().getTime())), "");
	 if(!StringUtils.isEmpty(result)) {
		 JSONObject obj = JSONObject.fromObject(result);
		 if(obj!=null) {
			 JSONObject data = (JSONObject) obj.get("data");
			 if(data!=null) {
				 JSONObject oas = data.getJSONObject("oaszc_ticker");
				 String oasValue = oas.getString("close");
				 Iterator<?> it = data.keys();
				 while(it.hasNext()) {
					String key  = (String) it.next();
					JSONObject base = data.getJSONObject(key);
					if(base.getString("quoteCurrencyName").equals("ZC") && key.indexOf("zc_ticker")!=-1) {
						String coinName = (String)base.get("baseCurrencyName");
						String value = base.getString("close");
						BigDecimal valueResult = new BigDecimal(value).divide(new BigDecimal(oasValue),4,BigDecimal.ROUND_HALF_UP); //币转换为oas,四舍五入保留4位小数
						if(key.equals("oaszc_ticker")) {
							if(exchangeRateMapper.selectOne(DateUtils.getDate(), CurrencyCode.CNY.getCode()) == null) {
								insertRate(DateUtils.getDate(), CurrencyCode.CNY, new BigDecimal(oasValue), comment);
							}
						}else {
							CurrencyCode nowC = CurrencyCode.getByName(coinName);
							if(exchangeRateMapper.selectOne(DateUtils.getDate(), nowC.getCode()) == null) {
								insertRate(DateUtils.getDate(), nowC , valueResult, comment);
							}
						}
					}
				 }
			 }
			 
		 }
	 }
  }

  public Integer setRate(String time, CurrencyCode currency, BigDecimal rate) {
    ExchangeRateModel exchangeRateModel = exchangeRateMapper.selectOne(time, currency.getCode());
    String now = DateUtils.getTime();
    if (exchangeRateModel == null) {
        return insertRate(time, currency, rate, "set");
    } else {
      exchangeRateModel.setUpdated(now);
      exchangeRateModel.setRate(rate);
      return exchangeRateMapper.updateSelective(exchangeRateModel);
    }
  }
  
  public ExchangeRateModel getRate(String time, CurrencyCode currency) {
    ExchangeRateModel exchangeRateModel = exchangeRateMapper.selectOne(time, currency.getCode());
    if(currency.getCode() == 1) {
    	if (exchangeRateModel == null) {
	      exchangeRateModel = exchangeRateMapper.selectNear(time, currency.getCode());
	      if (exchangeRateModel != null) {
	        insertRate(time, currency, exchangeRateModel.getRate(), "inherit");
	      }
	    }
    	exchangeRateModel = exchangeRateMapper.selectOne(time, currency.getCode());
    }else {
    	if (exchangeRateModel == null) {
    		this.getRatesFromNet("net");
    		exchangeRateModel = exchangeRateMapper.selectOne(time, currency.getCode());
    		if(exchangeRateModel == null) {
    			exchangeRateModel = exchangeRateMapper.selectNear(time, currency.getCode());
    		}
    	}
    }
    return exchangeRateModel;   
  }
  
  
  public ReturnValue<BigDecimal> exchangeTo(BigDecimal value, String time, CurrencyCode currency) {
    ExchangeRateModel rate = getRate(time, currency);
    ReturnValue<BigDecimal> returnValue = new  ReturnValue<>();
    if (rate == null) {
      returnValue.setErrorCode(ErrorCode.NO_AVAILABLE_EXCHANGE_RATE);
      returnValue.setData(BigDecimal.ZERO);
    } else {
      returnValue.setData(value.multiply(rate.getRate()));
      returnValue.setErrorCode(ErrorCode.SUCCESS);
    }
    return returnValue;
  }
   
  public ReturnValue<BigDecimal> exchangeFrom(BigDecimal value, String time, CurrencyCode currency) {
    ReturnValue<BigDecimal> returnValue = new  ReturnValue<>();
    ExchangeRateModel rate = getRate(time, currency);
    if (rate == null || rate.getRate().compareTo(BigDecimal.valueOf(0.00000001)) < 0) {
      returnValue.setErrorCode(ErrorCode.NO_AVAILABLE_EXCHANGE_RATE);
      returnValue.setData(BigDecimal.ZERO);
    } else {
      returnValue.setErrorCode(ErrorCode.SUCCESS);
      returnValue.setData(value.divide(rate.getRate(), 8, BigDecimal.ROUND_HALF_UP));
    }
    return returnValue;
  }
}
