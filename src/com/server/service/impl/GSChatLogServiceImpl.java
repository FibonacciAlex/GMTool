package com.server.service.impl;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.config.LogIOWriterParams;
import com.server.service.GSChatLogDataService;
import com.server.util.ServerLogFactory;

@Service
public class GSChatLogServiceImpl implements GSChatLogDataService{

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private String fileIoDirUrl;
	private String fileNameHead = "ChatLog";
	private String LogFormat = "%-3d %-10s %-10s %-1s";// 格式化LOG字符串行
	private LogIOWriterParams Params;// LogIO的参数
	
	@Override
	public void init(Element e) {
		
		fileIoDirUrl = e.getChildTextTrim("FileIODirUrl");
		String logFormat = e.getChildTextTrim("LogFormat");
		if (logFormat != null && logFormat.length() > 0) {
			this.LogFormat = logFormat;
		}

		int MaxFileLineSize = Integer.parseInt(e.getChildTextTrim("MaxFileLineSize"));
		int CacheMaxSize = Integer.parseInt(e.getChildTextTrim("CacheMaxSize"));
		long CacheFlushPeriod = Long.parseLong(e.getChildTextTrim("CacheFlushPeriod")) * 1000;

		String[] fileHeads = new String[] {// 文件头
		"-------------------------------------------------------------------", "[GS频道聊天日志]",
				"---------------------------以下是日志正文---------------------------" };

		Params = new LogIOWriterParams(LogIOWriterParams.IOTYPE_DAY_LINE, fileHeads, CacheFlushPeriod, CacheMaxSize,
				MaxFileLineSize);
		ServerLogFactory.mainLogger.info(">>>>> GM DB GS聊天模块 初始启动完成 URL=" + fileIoDirUrl);
	}

}
