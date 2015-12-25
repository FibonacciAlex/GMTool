package com.server.service.impl;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.service.GSNoticeService;
import com.server.util.ServerLogFactory;


/**
 * <pre>
 * GS公告管理
 * 每个GS大概创建小于1000行记录
 * 
 * 虚拟数据库,每个GS使用一个XML文件来记录;
 * 物理数据库,使用一个表来记录所有GS.
 * </pre>
 * 
 * 
 * @author Alex
 * @author 2015年12月23日 下午3:58:05
 */
@Service
public class GSNoticeServiceImpl implements GSNoticeService{

	private String fileIoDir;
	private String fileName = "Notice.xml";
	
	private ConcurrentHashMap<String, GSNotice> noticeMap = new ConcurrentHashMap<>();
	
	
	
	@Override
	public void init(Element e) {
		fileIoDir = e.getChildTextTrim("FileIODirUrl");
		ServerLogFactory.mainLogger.info(">>>>> GM DB GS公告模块 初始启动完成");
	}
	
	class GSNotice{
		private String gsName;
		private Properties propert;
		
		private AtomicBoolean isDirty = new AtomicBoolean(false);
		private AtomicLong idCreator;
		private AtomicBoolean isRun = new AtomicBoolean(true);
		private String fileUrl;
	}

}
