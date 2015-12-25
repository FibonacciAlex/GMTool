package com.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

public class ServerLogFactory {

		// 主流程日志
		public final static Logger mainLogger = LoggerFactory.getLogger("mainLogger");
		// 通讯日志
		public final static Logger netMsgLogger = LoggerFactory.getLogger("commLogger");

		// GMC操作日志
		private final static Logger gmcOpLogger = LoggerFactory.getLogger("gmOpLogger");
		// GS邮件操作日志
		private final static Logger gsMailLogger = LoggerFactory.getLogger("gsMailLogger");
		// GMC邮件日志
		private final static Logger gmcMailLogger = LoggerFactory.getLogger("gmcMailLogger");
		// GS聊天日志
		private final static Logger gsChatLogger = LoggerFactory.getLogger("gsChatLogger");

		/**
		 * <pre>
		 * GM操作日志
		 * GMS记录格式：收到内容(格式：GM名,GS名,GMC发送时间,GMC发送内容)
		 * 
		 * @param content
		 * @author CamusHuang
		 * @creation 2013-5-16 下午3:10:56
		 * </pre>
		 */
		public static void logGmcOpDebugLog(String content) {
			gmcOpLogger.debug(content);
		}

		/**
		 * <pre>
		 * GS邮件发送记录
		 * GMS记录格式：{GS名,角色名},GMC名,GS发送内容,记录时刻
		 * </pre>
		 */
		public static void logGsMailDebugLog(String gsname, String roleName, String content, String gmc) {
			gsMailLogger.debug("{{},{}},{},{},{}", gsname, roleName, gmc, content, System.currentTimeMillis());
		}

		/**
		 * <pre>
		 * GMC邮件回复记录
		 * 
		 * GMS记录格式：GMC名,{GS名,角色名},GMC发送内容,记录时刻
		 * </pre>
		 */
		public static void logGmcMailDebugLog(String gsname, String roleName, String content, String gmc) {
			gmcMailLogger.debug("{},{{},{}},{},{}", gmc, gsname, roleName, content, System.currentTimeMillis());
		}

		/**
		 * GS聊天记录 gsId,channelType,area,sender, receiver,content
		 * GMS记录格式：GSID,频道ID,地图或家族名,发送者,接收者,GS发送内容,接收时间
		 */
		public static void logGsChatDebugLog(int gsId, int channelType, String area, String sender, String receiver, String content,
				long nowTime) {
			gsChatLogger.debug("{},{},{},{},{},{},{}", GMTool.genGSName(gsId), channelType, area, sender, receiver, content, nowTime);
		}

		public static void shutdown() {
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
			lc.stop();
		}
}
