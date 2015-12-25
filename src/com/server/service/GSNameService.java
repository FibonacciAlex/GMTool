package com.server.service;

import java.util.Properties;

import org.jdom.Element;

public interface GSNameService {
	
	Properties propert = new Properties();

	void init(Element e) throws Exception;

}
