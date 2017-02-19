package com.gdut.dongjun.domain.dao;

import java.util.List;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;

public interface DeviceGroupMappingMapper extends SinglePrimaryKeyBaseMapper<DeviceGroupMapping> {

	public List<DeviceGroupMapping> selectByMappingId(Integer id);
 
}