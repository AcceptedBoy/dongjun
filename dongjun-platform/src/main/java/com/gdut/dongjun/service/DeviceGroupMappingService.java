package com.gdut.dongjun.service;

import java.util.List;

import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.service.base.BaseService;

public interface DeviceGroupMappingService extends BaseService<DeviceGroupMapping> {
	
	public List<DeviceGroupMapping> selectByMappingId(Integer id);

}
