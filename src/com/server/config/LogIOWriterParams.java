package com.server.config;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * 
 * <pre>
 *  专用于LogIOWriter的参数.可能多个LogIOWriter实例使用同一套参数实例
 * </pre>
 * 
 * @author Alex
 * @author 2015年12月21日 下午12:14:13
 */
public class LogIOWriterParams {

	
	/**
	 * <pre>
	 * IO模式 >>模式 IOTYPE_ONEFILE 表示不分文件,针对同一文件进行追溯 启动服务器时,若不存在文件,则新建 文件名格式如
	 * FileNameHead.txt
	 * 
	 * >>模式 IOTYPE_DAY_LINE 表示按日期,且按文件容量分文件;
	 * 启动服务器时,不追溯当天旧文件,直接根据文件编号新建当天分文件; 文件名格式如
	 * FileNameHead_2010-05-08_finleCount.txt
	 * 
	 * >>模式 IOTYPE_DAY 表示按日期分文件; 启动服务器时,追溯当天旧文件;若当天旧文件不存在,新建当天文件; 文件名格式如
	 * FileNameHead_2010-05-08.txt
	 * </pre>
	 */
	private int IOType;
	
	public final static int IOTYPE_ONEFILE = 1;
	public final static int IOTYPE_DAY_LINE = 2;
	public final static int IOTYPE_DAY = 3;
	
	/** 文件内容头缀 */
	private String[] FileHeads;
	/** 缓存保存到文件的周期 */
	private long CacheFlushPeriod;
	/** 缓存达到多少行时保存到文件 */
	private int CacheMaxSize;
	/** 文件达到多少行时分文件 */
	private int MaxFileLineSize;
	/** 文件日期格式 */
	private static SimpleDateFormat FileNameDateStringFormat = new SimpleDateFormat("yyyy-MM-dd");
	public LogIOWriterParams(int iotypeDay, String[] fileHeads2, long cacheFlushPeriod2, int cacheMaxSize2,
			int maxFileLineSize2) {
		this.IOType = iotypeDay;
		this.FileHeads = fileHeads2;
		this.CacheFlushPeriod = cacheFlushPeriod2;
		this.CacheMaxSize = cacheMaxSize2;
		this.MaxFileLineSize = maxFileLineSize2;
		switch (this.IOType) {
		case IOTYPE_ONEFILE:
		case IOTYPE_DAY:
			this.MaxFileLineSize = Integer.MAX_VALUE;
			break;
		default:
			break;
		}
				
	}

	static SimpleDateFormat FileNameDateStringFormat(){
		return (SimpleDateFormat)FileNameDateStringFormat.clone();
	}
	// ////////////////////////////////////////////// 固定参数
	/** 后台线程扫描缓存的周期 */
	private static final long ThreadPeriod = 5000;
	/** 随机偏移值 */
	private static final Random random = new Random();
	/** IO bufferSize */
	private static final int BufferSize = 200 * 1024;// 200K
	
	
	private long getThreadPeriod() {
		return ThreadPeriod + random.nextInt() % 1000;
	}

	private long getRandomCacheFlushPeriod() {
		return CacheFlushPeriod + (random.nextInt() % 5) * 1000;
	}

	private int getRandomCacheMaxSize() {
		int rd = (int) (CacheMaxSize * 0.05);
		if (rd < 2) {
			return CacheMaxSize;
		}
		return CacheMaxSize + random.nextInt() % rd;
	}


	
	
}
