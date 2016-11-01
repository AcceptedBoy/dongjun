package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureDeviceMapper;
import com.gdut.dongjun.domain.po.TemperatureDevice;
import com.gdut.dongjun.service.TemperatureDeviceService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class TemperatureDeviceServiceImpl extends BaseServiceImpl<TemperatureDevice> 
		implements TemperatureDeviceService {
	
	@Autowired
	private TemperatureDeviceMapper mapper;

	@Override
	protected boolean isExist(TemperatureDevice record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
	
}
