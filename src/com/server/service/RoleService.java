package com.server.service;

import java.util.List;

import com.server.model.Role;
import com.server.model.User;

public interface RoleService {

	/**
	 * 获取用户的角色列表
	 * @param u
	 * @return
	 */
	List<Role> selectRolesByUser(User u);
}
