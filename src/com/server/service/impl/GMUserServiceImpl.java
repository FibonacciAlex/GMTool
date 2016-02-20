package com.server.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.model.User;
import com.server.service.DBServiceFactory;
import com.server.service.GMUserService;
import com.server.util.CustomObjectMapper;
import com.server.util.JsonUtil;
import com.server.util.ServerLogFactory;

@Service
public class GMUserServiceImpl implements GMUserService{
	


	String fileName = "GMAccount.xml";

	String fileTitle = "GM用户数据";

	@Override
	public void init(Element e) throws Exception{
		String FileIODirUrl = e.getChildText("FileIODirUrl");
		
		DBServiceFactory.initPropertyData(FileIODirUrl, fileName,"GM用户数据", propert);
		
		
		ServerLogFactory.mainLogger.info(">>>>> GM DB GM用户模块 初始启动完成 URL=" + fileName);
	}

	@Override
	public User registerUser(String name, String pwd, String ca) throws Exception {
		try {
			
			User data = getUserByName(name);
			
			if(data != null){
				ServerLogFactory.mainLogger.warn("新增GM存在同名！拒绝新增");
				return null;
			}
			data = new User(name, pwd, ca);
			String json = JsonUtil.generateJsonString(data);
			propert.put(ElementKey + name, json);
			
			DBServiceFactory.save(fileName, fileTitle, propert);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			ServerLogFactory.mainLogger.error(e.getMessage(),e);
		}
		return null;
	}
	
	

	@Override
	public User getUserByName(String name){
		User data = null;
		for (Iterator<Object> itr = propert.keySet().iterator(); itr.hasNext();) {
			String key = (String) itr.next();
			String value = propert.getProperty(key);
			if (key.startsWith(ElementKey)) {
				try {
					String xmlGmName = key.replaceFirst(ElementKey, "");
					if(xmlGmName.equals(name)){
						data = JsonUtil.parseFromJson(value, User.class);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ServerLogFactory.mainLogger.error(e.getMessage(),e);
				}
			}
		}
		return data;
	}

	
}
