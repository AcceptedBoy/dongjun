package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.DeviceGroupMappingMapper;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class DeviceGroupMappingServiceImpl extends BaseServiceImpl<DeviceGroupMapping> implements DeviceGroupMappingService {

	@Autowired
	DeviceGroupMappingMapper mapper;
	
	@Override
	protected boolean isExist(DeviceGroupMapping record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId() + "") != null))
			return true;
		return false;
	}

}
