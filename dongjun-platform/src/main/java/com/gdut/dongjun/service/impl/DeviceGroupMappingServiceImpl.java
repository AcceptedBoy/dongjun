package com.gdut.dongjun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.DeviceGroupMappingMapper;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class DeviceGroupMappingServiceImpl extends EnhancedServiceImpl<DeviceGroupMapping> implements DeviceGroupMappingService {

	@Autowired
	DeviceGroupMappingMapper mapper;
	
	@Override
	protected boolean isExist(DeviceGroupMapping record) {
		if (record != null && record.getId() != null
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}

	@Override
	public List<DeviceGroupMapping> selectByMappingId(Integer id) {
		return mapper.selectByMappingId(id);
	}

}
