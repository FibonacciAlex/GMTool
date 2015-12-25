package com.server.service;

import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jdom.Element;

import com.server.model.User;

public interface GMUserService {

	
	Properties propert = new Properties();;
	
	String ElementKey = "gm_";
	

	/**
	 * 新增gm  
	 * 如果成功会返回新增的GM数据，
	 * 如果不成功会返回null;
	 * @param name
	 * @param pwd
	 * @param ca
	 * @return
	 * @throws Exception
	 */
	User registerUser(String name, String pwd, String ca) throws Exception;


	void init(Element child) throws Exception;


	/**
	 * 通过角色名获取角色数据
	 * @param userName
	 * @return
	 */
	User getUserByName(String userName);
}
