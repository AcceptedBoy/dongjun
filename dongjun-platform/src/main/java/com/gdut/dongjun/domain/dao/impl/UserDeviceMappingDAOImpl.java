package com.gdut.dongjun.domain.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.UserDeviceMappingMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.UserDeviceMapping;

@Repository
public class UserDeviceMappingDAOImpl extends SinglePrimaryKeyBaseDAOImpl<UserDeviceMapping> 
		implements UserDeviceMappingMapper {

	@Override
	public List<UserDeviceMapping> selectMappingEnableToSeeByUserId(String userId) {
		return template.selectList(getNamespace("selectMappingEnableToSeeByUserId"), userId);
	}

	@Override
	public List<UserDeviceMapping> selectMappingEnableToGenerateByUserId(String userId) {
		return template.selectList(getNamespace("selectMappingEnableToGenerateByUserId"), userId);
	}

}
