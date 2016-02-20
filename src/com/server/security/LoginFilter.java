package com.server.security;

import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import com.server.util.ServerLogFactory;

/**
 * <pre>
 * 自定义的登录过滤器
 * 
 * </pre>
 * 
 * @author Alex
 * @author 2015年12月29日 下午7:51:40
 */
public class LoginFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		ServerLogFactory.mainLogger.info("----------------------------------check login filter~");
		Subject subject = getSubject(request, response);
		String [] rolesArray = (String[]) mappedValue;
		if(rolesArray == null || rolesArray.length == 0){
			ServerLogFactory.mainLogger.info("no mappedvalue!");
			return true;
		}
		Set<String> asSet = CollectionUtils.asSet(rolesArray);
		
		for (String string : asSet) {
			if(subject.hasRole(string)){
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		ServerLogFactory.mainLogger.info("------------------------------login filter access denied~");

		return true;
	}

}
