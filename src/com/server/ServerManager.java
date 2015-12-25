package com.server;

import java.io.File;
import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;

import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.ContextLoaderListener;
import com.server.gs.GSManager;
import com.server.service.DBServiceFactory;
import com.server.util.ServerLogFactory;
import com.server.util.UtilTool;


//public class ServerManager implements ServletContextListener{
public class ServerManager extends ContextLoaderListener{

	private final static Logger log = LoggerFactory.getLogger(ServerManager.class);
	
	//采取这个方式无法注入
//	@Resource
//	DBServiceFactory serviceFactory;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("-----------System shutdown~~注意，配置文件有改动也会触发这个方法！！");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			log.debug("---------------!!!!!!!!!!!!!!!!!!!!System start~~");
			
			String parameter = arg0.getServletContext().getInitParameter("baseLogicCfg");
			
			//file:/D:/Project/EclipseWeb/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/GameTool/WEB-INF/classes/logback.xml
			
			//file:/D:/Project/EclipseWeb/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/GameTool/WEB-INF/classes/GMServerConfig.xml
			log.info("--------------baseLogicCfg::[{}]", parameter);
			File file = ResourceUtils.getFile(parameter);
			Document doc = UtilTool.openXml(file);
			Element root = doc.getRootElement();
			
			
			ServerLogFactory.mainLogger.info("***********  开始初始化【DB】 ******************");
			Element dbelement = root.getChild("DBSetting");
			DBServiceFactory serviceFactory = (DBServiceFactory) getCurrentWebApplicationContext().getBean("dbServiceFactory");
			//通过ApplicationContext 来获取serviceFactory 是因为这个时候无法将DBServiceFactory作为一个变量进行注入   原因如
			//1 注入的方法不对  所以采取了折中的方法，从配置文件中注入
			//2 这个时候确实无法注入，因为此类配置在web文件中，作为启动监听，所有的bean还没有初始化完成，所以无法注入
			serviceFactory.init(dbelement);
			
			
			
			
			ServerLogFactory.mainLogger.info("*************  开始初始化 【网络】 ***********");
			initNet(root.getChild("NetSetting"));
			
//			WebApplicationContext context2 = getCurrentWebApplicationContext();
//			NetConfig bean = (NetConfig) context2.getBean("NetSetting");
//			GSManager gsManager = (GSManager) context2.getBean("GSManager");
//			log.info("------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-get GMSConfig bean ? :[{}]", bean != null);
//			initNet(bean);
			//start
//			GameGM2FE.getInstance().start(gsManager);
			
		} catch (Exception e) {
			e.printStackTrace();
			ServerLogFactory.mainLogger.error("************GMS System start fail*****");
			System.exit(1);//TODO 这里应该是通知tomcat停下
			
		}
		
		
		
	}


	/**
	 * 初始化网络配置及启动网络连接
	 * @param element
	 * @throws Exception 
	 */
	private void initNet(Element element) throws Exception {
		
		//初始化GM2FE网络配置
		GameGM2FE.getInstance().initNetConfig(element.getChild("FENetSetting"));
		
		//初始化GM2GS网络配置
		GameGM2GS.getInstance().initNetConfig(element.getChild("GSNetSetting"));
		
		
		//启动网络
		GSManager bean = (GSManager) getCurrentWebApplicationContext().getBean("GSManager");
		GameGM2FE.getInstance().start(bean);
	}

	
}
