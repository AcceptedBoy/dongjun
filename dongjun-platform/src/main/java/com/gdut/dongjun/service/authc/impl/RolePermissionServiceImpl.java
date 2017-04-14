package com.gdut.dongjun.service.authc.impl;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.authc.RolePermission;
import com.gdut.dongjun.service.authc.RolePermissionService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class RolePermissionServiceImpl extends EnhancedServiceImpl<RolePermission>
	implements RolePermissionService {


	@Override
	protected boolean isExist(RolePermission record) {
		return true;
	}
}
