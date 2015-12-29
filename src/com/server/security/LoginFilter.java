package com.server.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.server.util.ServerLogFactory;


/**
 * <pre>
 * 自定义的登录过滤器
 * 
 * </pre>
 * @author Alex
 * @author 2015年12月29日 下午7:51:40
 */
public class LoginFilter extends AccessControlFilter{

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		ServerLogFactory.mainLogger.info("----------------------------------login filter allowed~");
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		ServerLogFactory.mainLogger.info("------------------------------login filter access denied~");
		
		return false;
	}

}
