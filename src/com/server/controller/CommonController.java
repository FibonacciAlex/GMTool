package com.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.server.config.SystemConstant;

/**
 * 
 * 公共视图控制器
 * 
 * @author Alex
 * @author 2015年12月23日 下午5:49:39
 */
@Controller
public class CommonController {

	/**
	 * 首页
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request){
		
		//这里应该要判断一下，如果登录了，进入index,如果没有登录，返回login页面
		HttpSession session = request.getSession();
		Object object = session.getAttribute(SystemConstant.USERNAME);
		if(object == null){
			return "login";
		}
		return "index";
	}
	
}
