package com.gdut.dongjun.service.authc.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.authc.RoleMapper;
import com.gdut.dongjun.domain.po.authc.Role;
import com.gdut.dongjun.service.authc.RoleService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

/**
 * @author Sherlock-lee
 * @date 2015年9月30日 上午2:34:39
 * @see TODO
 * @since 1.0
 */
@Service
public class RoleServiceImpl extends EnhancedServiceImpl<Role> implements
		RoleService {

	@Resource
	private RoleMapper roleMapper;

	@Override
	public List<Role> selectByUserId(String userId) {

		return roleMapper.selectByUserId(userId);
	}

	@Override
	protected boolean isExist(Role record) {
		//TODO
		return true;
	}

}
