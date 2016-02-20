package com.server.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.server.config.SystemConstant;
import com.server.model.User;
import com.server.service.GMUserService;
import com.server.util.Response;

/**
 * 用户控制器
 * @author Alex
 * @author 2015年12月24日 下午6:16:43
 */
@RestController
@RequestMapping(value="/user")
public class UserController {
	
	@Resource
	private GMUserService userService;

	/**
	 * 登录
	 * @return
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public Response login(@RequestBody User param){
		
		Response rs = null;
		
		Subject subject = SecurityUtils.getSubject();
		String userName = param.getUserName();
		String password = param.getPwd();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		try {
			
			subject.login(token);
			rs = new Response().success();
		} catch (Exception e) {
			rs = new Response().failure("用户名/密码错误！");
		}
		return rs;
	}
	
	/**
     * 用户登出
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Response logout(HttpSession session) {
        session.removeAttribute("userInfo");
        
        SecurityUtils.getSubject().login(null);
        // 登出操作
        return new Response().success();
    }
    
    

    
}
