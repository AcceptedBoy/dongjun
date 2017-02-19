package com.gdut.dongjun.domain.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.DeviceGroupMappingMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;

@Repository
public class DeviceGroupMappingDAOImpl extends SinglePrimaryKeyBaseDAOImpl<DeviceGroupMapping> implements DeviceGroupMappingMapper {

	@Override
	public List<DeviceGroupMapping> selectByMappingId(Integer id) {
		return template.selectList(getNamespace("selectByMappingId"), id); 
	}

}