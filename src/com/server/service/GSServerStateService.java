package com.server.service;

import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Element;

import com.server.util.LogIOWriter;

public interface GSServerStateService {

	ConcurrentHashMap<String, LogIOWriter> logIOMap = new ConcurrentHashMap<>();
	
	
	void init(Element element);

}
