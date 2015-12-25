package com.server.service;

import java.util.List;

import com.server.model.Permission;

/**
 * 权限接口
 * @author Alex
 * @author 2015年12月25日 下午5:57:59
 */
public interface PermissionService {

	List<Permission> getPermissionsByRoleID(long roleID);
}
