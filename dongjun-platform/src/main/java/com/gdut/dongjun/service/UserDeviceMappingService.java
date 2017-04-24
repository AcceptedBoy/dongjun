package com.gdut.dongjun.service;

import java.util.List;

import com.gdut.dongjun.domain.po.UserDeviceMapping;
import com.gdut.dongjun.service.base.EnhancedService;

public interface UserDeviceMappingService extends EnhancedService<UserDeviceMapping> {

	public List<UserDeviceMapping> selectMappingEnableToSeeByUserId(String id);
	
	public List<UserDeviceMapping> selectMappingEnableToGenerateByUserId(String id);

	public List<String> selectMonitorIdByUserId(String id);
}
