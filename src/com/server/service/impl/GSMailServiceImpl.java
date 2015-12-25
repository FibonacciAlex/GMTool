package com.server.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.config.LogIOWriterParams;
import com.server.service.GSMailService;
import com.server.util.ServerLogFactory;

@Service
public class GSMailServiceImpl implements GSMailService{

	private String fileIODir;
	private int logKeepDays;
	private String fileNameHead = "MailLog";
	private String startLine;
	private LogIOWriterParams params;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	/** 所有GS的邮件数据 key=gsName */
	private ConcurrentHashMap<String, GSMailData> mailDataMap = new ConcurrentHashMap<>();
	
	@Override
	public void init(Element e) {
		fileIODir = e.getChildTextTrim("FileIODirUrl");
		logKeepDays = Integer.parseInt(e.getChildTextTrim("LogKeepDays"));

		int MaxFileLineSize = Integer.parseInt(e.getChildTextTrim("MaxFileLineSize"));
		int CacheMaxSize = Integer.parseInt(e.getChildTextTrim("CacheMaxSize"));
		long CacheFlushPeriod = Long.parseLong(e.getChildTextTrim("CacheFlushPeriod")) * 1000;

		// //////////////////////////////////////////////////////////////////////
		String[] fileHeads = new String[] {// 文件头
		"-------------------------------------------------------------------", "[GS邮件日志]",
				"---------------------------以下是日志正文---------------------------" };
		startLine = fileHeads[fileHeads.length - 1];

		params = new LogIOWriterParams(LogIOWriterParams.IOTYPE_DAY, fileHeads, CacheFlushPeriod, CacheMaxSize, MaxFileLineSize);
		ServerLogFactory.mainLogger.info(">>>>> GM DB GS邮件模块 初始启动完成");
	}

	
	class GSMailData{
		
	}
}
