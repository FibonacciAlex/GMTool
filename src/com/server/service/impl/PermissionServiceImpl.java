package com.server.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.server.model.Permission;
import com.server.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService{

	@Override
	public List<Permission> getPermissionsByRoleID(long roleID) {
		return null;
	}

}
