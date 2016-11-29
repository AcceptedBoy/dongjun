package com.gdut.dongjun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.DeviceGroupMapper;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class DeviceGroupServcieImpl extends BaseServiceImpl<DeviceGroup> implements DeviceGroupService {

	@Autowired
	private DeviceGroupMapper deviceGroupMapper;
	
	@Override
	protected boolean isExist(DeviceGroup record) {
		if (null == record.getId()) {
			return false;
		}
		DeviceGroup group = deviceGroupMapper.selectByPrimaryKey(record.getId());
		if (group == null) {
			return false;
		}
		return true;
	}
	

}
