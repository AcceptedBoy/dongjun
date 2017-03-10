package com.gdut.dongjun.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.UserDeviceMappingMapper;
import com.gdut.dongjun.domain.po.UserDeviceMapping;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class UserDeviceMappingServiceImpl extends BaseServiceImpl<UserDeviceMapping>
			implements UserDeviceMappingService {

	@Autowired
	private UserDeviceMappingMapper mapper;
	
	@Override
	protected boolean isExist(UserDeviceMapping record) {
		if (record != null
				&& mapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<UserDeviceMapping> selectMappingEnableToSeeByUserId(String id) {
		return mapper.selectMappingEnableToSeeByUserId(id);
	}

	@Override
	public List<UserDeviceMapping> selectMappingEnableToGenerateByUserId(String id) {
		return mapper.selectMappingEnableToGenerateByUserId(id);
	}

}
