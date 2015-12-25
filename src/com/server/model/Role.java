package com.server.model;

import org.apache.shiro.subject.Subject;

/**
 * <pre>
 * 角色模型
 * 
 * 在shiro内，用户{@link Subject}为主体，对应系统内的{@link User}
 * 一个用户可能有多个角色{@link Role}，一个角色有多个权限{@link Permission}
 * 用户只有获取到权限后才可以访问资源{@link Resource}
 * 
 * 可以将角色看作权限的集合
 * 
 * </pre>
 * @author Alex
 * @author 2015年12月25日 下午5:34:14
 */
public class Role {

	/**
	 * 返回角色标记
	 * @return
	 */
	public String getRoleSign() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取角色id
	 * @return
	 */
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
