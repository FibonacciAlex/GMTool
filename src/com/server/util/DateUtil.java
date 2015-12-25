package com.server.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtil {

	
	/**返回时间格式yyyyMMddHHmmss*/
	public static final DateFormat FORMAT_DEFAULT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**返回时间格式 yyyy-MM-dd HH:mm:ss*/
	public static final DateFormat FORMAT_READABILITY = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
