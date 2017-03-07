package com.gdut.dongjun.domain.dao;

import java.util.List;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.UserDeviceMapping;

public interface UserDeviceMappingMapper extends SinglePrimaryKeyBaseMapper<UserDeviceMapping> {
    
	public List<UserDeviceMapping> selectMappingAbleToSeeByUserId(String userId);
	
	public List<UserDeviceMapping> selectMappingAbleToGenerateByUserId(String userId);
}