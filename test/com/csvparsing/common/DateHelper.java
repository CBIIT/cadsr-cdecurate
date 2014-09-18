package com.csvparsing.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdf.format(now);
	    return strDate;
	}

}