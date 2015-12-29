package com.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.server.config.SystemConstant;
import com.server.model.User;

/**
 * 用户控制器
 * @author Alex
 * @author 2015年12月24日 下午6:16:43
 */
@Controller
@RequestMapping(value="/user")
public class UserController {

	/**
	 * 登录
	 * @return
	 */
	@RequestMapping(value="/loginServer", method = RequestMethod.POST)
	public String login(User user, BindingResult result, Model model, HttpServletRequest request){
		try {
			
			Subject subject = SecurityUtils.getSubject();
			if(subject.isAuthenticated()){
				return "redirect:/main";  //直接redirect  避免用户刷新页面时重复提交表单
			}
			if(result.hasErrors()){
				model.addAttribute("error", "参数错误！");
				return "login";
			}
			//验证
			subject.login(new UsernamePasswordToken(user.getUserName(), user.getPwd()));
			//保存用户信息
			request.getSession().setAttribute(SystemConstant.USERNAME, user);
			
		} catch (Exception e) {
			// 验证失败
			model.addAttribute("error", "用户名或密码错误");
			return "login";
		}
		return "redirect:/main";
	}
	
	/**
     * 用户登出
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute("userInfo");
        // 登出操作
        return "login";
    }
    
}
