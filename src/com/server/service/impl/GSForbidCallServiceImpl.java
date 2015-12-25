package com.server.service.impl;

import org.jdom.Element;
import org.springframework.stereotype.Service;

import com.server.service.DBServiceFactory;
import com.server.service.GSForbidCallService;
import com.server.util.ServerLogFactory;


/**
 * <pre>
 * 管理禁呼角色
 * 即时保存
 * </per>
 * @author Alex
 * @author 2015年12月21日 下午7:58:53
 */
@Service
public class GSForbidCallServiceImpl implements GSForbidCallService{

	private String fileName = "ForbidCallGM.xml";
	
	private String fileTitle = "GM禁呼数据";
	
	@Override
	public void init(Element e) throws Exception{
		String fileDir = e.getChildTextTrim("FileIODirUrl");
		DBServiceFactory.initPropertyData(fileDir, fileName, fileTitle, propert);

		ServerLogFactory.mainLogger.info(">>>>> GM DB GS禁呼模块 初始启动完成 URL=" + fileName);
	}

}
