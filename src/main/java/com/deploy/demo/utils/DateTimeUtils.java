package com.deploy.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DateTimeUtils {
	
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS";
	public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
	public static final String HARRIS_DATE_FORMAT = "ddMMyy";

	public DateTimeUtils() {}


	
	public static synchronized String getCurrentDateTime(Date date) {
		if(date == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		return dateFormat.format(date);
	}
	
	public static synchronized Date getCurrentDateTime(String date) throws ParseException {
		if(date == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		return dateFormat.parse(date);
	}


}
