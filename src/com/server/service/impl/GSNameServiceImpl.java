package com.server.service.impl;

import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.service.DBServiceFactory;
import com.server.service.GSNameService;

@Service
public class GSNameServiceImpl implements GSNameService{

	private String fileName = "GSName.xml";
	
	private String fileTitle = "GS名称数据";
	
	@Override
	public void init(Element e) throws Exception{
		String fileDir = e.getChildTextTrim("FileIODirUrl");
		DBServiceFactory.initPropertyData(fileDir, fileName, fileTitle, propert);
	}

}
