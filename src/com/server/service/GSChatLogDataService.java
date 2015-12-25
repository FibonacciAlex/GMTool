package com.server.service;

import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Element;

import com.server.util.LogIOWriter;

public interface GSChatLogDataService {

	/** GS聊天频道日志缓存 key=gsName */
	ConcurrentHashMap<String, LogIOWriter> logIoMap = new ConcurrentHashMap<>();
	
	void init(Element element);

}
