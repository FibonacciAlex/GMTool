package com.server.service.impl;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.config.LogIOWriterParams;
import com.server.service.GSServerStateService;
import com.server.util.ServerLogFactory;


@Service
public class GSServerStateImpl implements GSServerStateService{

	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private String fileDirUrl;
	private String fileNameHead = "StateLog";
	private String StartLine;
	private LogIOWriterParams params;
	
	@Override
	public void init(Element e) {
		fileDirUrl = e.getChildTextTrim("FileIODirUrl");


		int MaxFileLineSize = Integer.parseInt(e.getChildTextTrim("MaxFileLineSize"));
		int CacheMaxSize = Integer.parseInt(e.getChildTextTrim("CacheMaxSize"));
		long CacheFlushPeriod = Long.parseLong(e.getChildTextTrim("CacheFlushPeriod")) * 1000;

		String[] fileHeads = new String[] {// 文件头
		"-------------------------------------------------------------------", "[GS状态日志]",
				"---------------------------以下是日志正文---------------------------" };
		StartLine = fileHeads[fileHeads.length - 1];

		params = new LogIOWriterParams(LogIOWriterParams.IOTYPE_DAY, fileHeads, CacheFlushPeriod, CacheMaxSize, MaxFileLineSize);
		
		ServerLogFactory.mainLogger.info(">>>>> GM DB GS状态模块 初始启动完成");
	}

}
