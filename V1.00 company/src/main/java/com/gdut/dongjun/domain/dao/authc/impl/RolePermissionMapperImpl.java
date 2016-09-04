package com.gdut.dongjun.domain.dao.authc.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.authc.RolePermissionMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.authc.RolePermission;

@Repository
public class RolePermissionMapperImpl extends SinglePrimaryKeyBaseDAOImpl<RolePermission>
	implements RolePermissionMapper {

}
