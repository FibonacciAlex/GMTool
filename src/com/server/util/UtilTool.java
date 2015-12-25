package com.server.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

public class UtilTool {

	
	public static Document openXml(String xml){
		File file = new File(xml);
		if(!file.exists()){
			ServerLogFactory.mainLogger.error("文件[{}]不存在~~",xml);
			return null;
		}
		return openXml(file);
	}

	public static Document openXml(File file) {
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		try {
			doc = sb.build(file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}


	
	
}
