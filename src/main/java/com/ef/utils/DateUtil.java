package com.ef.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
	
	public static final String PARAMETER_START_DATE_PATTERN = "yyyy-MM-dd.HH:mm:ss";
	public static final String LOG_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String STRING_TO_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	
	public static final SimpleDateFormat PARAMETER_START_DATE = new SimpleDateFormat(PARAMETER_START_DATE_PATTERN);
	public static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat(LOG_DATE_PATTERN);
	
	 public static Date addHours(Date oldDate, int hours) {
		    return new Date(oldDate.getTime() + TimeUnit.HOURS.toMillis(hours));
		  }
	 
	 public static String dateToString(Date date){
		 SimpleDateFormat sf = new SimpleDateFormat(STRING_TO_DATE_PATTERN);
		 return  sf.format(date);
		 
	 }

}
