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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.server.config.SystemConstant;
import com.server.model.User;
import com.server.service.GMUserService;

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
	@RequestMapping(value="/loginServer", method = RequestMethod.POST)
	public String login(Model model, HttpServletRequest request){
		//@RequestBody 注解定义需要反序列化的参数 , 将浏览器发过来的JSON 参数转换为普通的 Java 对象
		try {
			Subject subject = SecurityUtils.getSubject();
			if(subject.isAuthenticated()){
				return "redirect:/main";  //直接redirect  避免用户刷新页面时重复提交表单
			}
//			if(result.hasErrors()){
//				model.addAttribute("error", "参数错误！");
//				return "redirect:/login";
//			}
			
			String name = request.getParameter("username");
			String pwd = request.getParameter("password");
			User user = new User(name, pwd, null);
			//验证
			subject.login(new UsernamePasswordToken(user.getUserName(), user.getPwd()));
			//保存用户信息
			request.getSession().setAttribute(SystemConstant.USERNAME, user);
			
		} catch (Exception e) {
			// 验证失败
			model.addAttribute("error", "用户名或密码错误");
			return "redirect:/login";
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
        return "redirect:/login";
    }
    
    

    
}
