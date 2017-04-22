package com.gdut.dongjun.service;

import java.util.List;

import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.service.base.EnhancedService;

public interface DeviceGroupMappingService extends EnhancedService<DeviceGroupMapping> {
	
	public List<DeviceGroupMapping> selectByMappingId(Integer id);

}
