package com.server.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.server.model.Role;
import com.server.model.User;
import com.server.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Override
	public List<Role> selectRolesByUser(User u) {
		return null;
	}

}
