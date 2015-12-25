package com.server.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.annotation.Resource;

import org.jdom.Element;
import com.server.util.ServerLogFactory;


public class DBServiceFactory {

	
	/* 关于GM帐号 */
	@Resource
	private GMUserService userService;
	
	/* 关于GM操作日志 */
	@Resource
	private GMOperationService gmOperationService;
	
	/* GS名称管理 */
	@Resource
	private GSNameService gsNameService;
	
	/* 关于GS聊天日志 */
	@Resource
	private GSChatLogDataService gsChatLogService;
	
	/* 关于GS状态日志 */
	@Resource
	private GSServerStateService gsServerStateService;
	
	/* 关于GS公告 */
	@Resource
	private GSNoticeService gsNoticeService;
	
	/** 关于GS邮件 */
	@Resource
	private GSMailService gsMailService;
	
	/*** 禁呼数据保存 */
	@Resource
	private GSForbidCallService gsForbidCallService;
	
	/** 关于GM统计日志 */
	@Resource
	private GMWorkDataService gmWorkDataService;
	

	
	public void init(Element e) throws Exception {
		userService.init(e.getChild("GMUserDataAccess"));
		gmOperationService.init(e.getChild("GMOpDataAccess"));
		gsNameService.init(e.getChild("GSNameDataAccess"));
		gsChatLogService.init(e.getChild("GSChatLogDataAccess"));
		gsServerStateService.init(e.getChild("GSStateDataAccess"));
		gsNoticeService.init(e.getChild("GSNoticeDataAccess"));
		gsMailService.init(e.getChild("GSMailDataAccess"));
		gsForbidCallService.init(e.getChild("GSForbidCallAccess"));
		gmWorkDataService.init(e.getChild("GMWorkDataAccess"));
	}


	public static void initPropertyData(String dirUrl, String fileName, String fileTitle, Properties propert)
			throws Exception {
		File file = new File(dirUrl);
		if (!file.exists()) {
			file.mkdirs();
		}

		fileName = dirUrl + fileName;
		file = new File(fileName);
		// 创建文件
		if (!file.exists()) {

			FileOutputStream fos;
			fos = new FileOutputStream(fileName);
			propert.storeToXML(fos, fileTitle, "UTF-8");
			fos.close();
		}

		// 读入文件
		FileInputStream fis;
		fis = new FileInputStream(fileName);
		propert.loadFromXML(fis);
		fis.close();
	}
	

	/**
	 * 保存数据
	 * @param fileName
	 * @param fileTitle
	 * @param propert
	 */
	public static void save(String fileName, String fileTitle, Properties propert) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			propert.storeToXML(fos, fileTitle, "UTF-8");
			fos.close();
		} catch (Exception e) {
			ServerLogFactory.mainLogger.error(e.getMessage(),e);
		}
		
	}
	
	
}
