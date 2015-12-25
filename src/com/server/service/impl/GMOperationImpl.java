package com.server.service.impl;

import java.util.Date;

import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.config.LogIOWriterParams;
import com.server.service.GMOperationService;
import com.server.util.ServerLogFactory;

@Service
public class GMOperationImpl implements GMOperationService{

	private String fileIoDirUrl;
	private int logKeepDays;
	private String fileNameHead = "GmOPLog";
	private String StartLine;//日志文本中正文起始行
	private LogIOWriterParams params; //logio 的参数
	
	@Override
	public void init(Element e) {
		fileIoDirUrl = e.getChildTextTrim("FileIODirUrl");
		
		logKeepDays = Integer.parseInt(e.getChildTextTrim("LogKeepDays"));

		int MaxFileLineSize = Integer.parseInt(e.getChildTextTrim("MaxFileLineSize"));
		int CacheMaxSize = Integer.parseInt(e.getChildTextTrim("CacheMaxSize"));
		long CacheFlushPeriod = Long.parseLong(e.getChildTextTrim("CacheFlushPeriod")) * 1000;

		// //////////////////////////////////////////////////////////////////////
		String[] fileHeads = new String[] {// 文件头
		"-------------------------------------------------------------------", "[GM操作日志]",
				"---------------------------以下是日志正文---------------------------" };
		StartLine = fileHeads[fileHeads.length - 1];

		params = new LogIOWriterParams(LogIOWriterParams.IOTYPE_DAY, fileHeads, CacheFlushPeriod, CacheMaxSize, MaxFileLineSize);
	
		ServerLogFactory.mainLogger.info(">>>>> GM DB GM操作日志模块 初始启动完成 URL=" + fileIoDirUrl + " " + new Date().toString());
	}

}
