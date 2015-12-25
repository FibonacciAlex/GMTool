package com.server.service.impl;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.config.LogIOWriterParams;
import com.server.service.GMWorkDataService;
import com.server.util.LogIOWriter;
import com.server.util.ServerLogFactory;

@Service
public class GMWorkServiceImpl implements GMWorkDataService{

	private LogIOWriter logWriter;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private String fileIODir;
	
	private String fileNameHead = "GMWorkData";
	
	private String startLine;
	
	private LogIOWriterParams params;
	
	@Override
	public void init(Element e) {
		fileIODir = e.getChildTextTrim("FileIODirUrl");

        int MaxFileLineSize = Integer.parseInt(e.getChildTextTrim("MaxFileLineSize"));
        int CacheMaxSize = Integer.parseInt(e.getChildTextTrim("CacheMaxSize"));
        long CacheFlushPeriod = Long.parseLong(e.getChildTextTrim("CacheFlushPeriod")) * 1000;

        String[] fileHeads = new String[]{//文件头
            "-------------------------------------------------------------------",
            "[GM统计日志]",
            "---------------------------以下是日志正文---------------------------"
        };
        startLine = fileHeads[fileHeads.length - 1];

        params = new LogIOWriterParams(
                LogIOWriterParams.IOTYPE_DAY, fileHeads,
                CacheFlushPeriod,
                CacheMaxSize,
                MaxFileLineSize);
		ServerLogFactory.mainLogger.info(">>>>> GM DB GM统计日志模块 初始启动完成 ");
	}

}
