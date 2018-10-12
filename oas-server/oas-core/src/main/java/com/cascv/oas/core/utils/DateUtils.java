package com.cascv.oas.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils {
    public static String YYYY = "yyyy";
    public static String YYYY_MM = "yyyy-MM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYYMMDD = "yyyyMMdd";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static Date getNowDate() {
       return new Date();
    }
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)  {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
    	try {
        return new SimpleDateFormat(format).parse(ts);
    	} catch (ParseException e){
        throw new RuntimeException(e);
    	}
    }

    
    // 2018/08/08
    public static final String datePath() {
       Date now = new Date();
       return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    
    // 20180808
    public static final String dateTime() {
       Date now = new Date();
       return DateFormatUtils.format(now, "yyyyMMdd");
    }
    
    /**
     * @author Ming Yang
     * 日期(时间)转化为字符串.
     * @param formater
     * 			日期或时间的格式.
     * @param aDate
     * 			java.util.Date类的实例.
     * @return 日期转化后的字符串.
     */
    public static String date2String(String formater, Date aDate) {
    	if (formater == null || "".equals(formater))
    	return null;
    	if (aDate == null)
    	return null;
    	return (new SimpleDateFormat(formater)).format(aDate);
    }
    
    /**
     * @author Ming Yang
      * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)
     * @param timeZoneId
     * 				时区Id
     * @return 系统当前默认时区与指定时区的时间差.(单位:毫秒)
     */
    private static int getDiffTimeZoneRawOffset(String timeZoneId) { 
    	
    	return TimeZone.getDefault().getRawOffset()- TimeZone.getTimeZone(timeZoneId).getRawOffset();
    	
    }
    
    /**
     * @author Ming Yang
     * 当前日期(时间)转化为字符串. 
     * @param formater
     * 			日期或时间的格式.
     * @return 日期转化后的字符串.
     */
    public static String date2String(String formater) {
    	return date2String(formater, new Date());
    }
    
    /**
     * @author Ming Yang
     * 将日期时间字符串根据转换为指定时区的日期时间.
     * 
     * @param srcFormater
     * 			待转化的日期时间的格式.
     * @param srcDateTime
     * 			待转化的日期时间.
     * @param dstFormater
     * 			目标的日期时间的格式.
     * @param dstTimeZoneId
     * 			目标的时区编号.
     * 
     * @return 转化后的日期时间.
     */
    public static String string2Timezone(String srcFormater,String srcDateTime, String dstFormater, String dstTimeZoneId) {
    	srcFormater=YYYY_MM_DD_HH_MM_SS;
    	dstFormater=YYYY_MM_DD_HH_MM_SS;
    	if (srcDateTime == null || "".equals(srcDateTime))
    		return null;
    	if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
    		return null;
    	SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
    	try {
    		int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);
    		System.out.println("****与目标时区时间差****单位毫秒");
    		System.out.println(diffTime);
    		System.out.println("****与目标时区时间差****单位毫秒");
    		Date d = sdf.parse(srcDateTime);
    		long nowTime = d.getTime();
    		long newNowTime = nowTime - diffTime;
    		d = new Date(newNowTime);
    		return date2String(dstFormater, d);
    	}catch(ParseException e) {
    		System.out.println(e.toString());
    		return null;
    	}finally {
    		sdf=null;
    	}
    }

}
