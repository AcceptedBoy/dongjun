package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureSensorMapper;
import com.gdut.dongjun.domain.po.TemperatureSensor;
import com.gdut.dongjun.service.TemperatureSensorService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class TemperatureSensorServiceImpl extends EnhancedServiceImpl<TemperatureSensor> implements TemperatureSensorService {

	@Autowired
	TemperatureSensorMapper mapper;
	
	@Override
	protected boolean isExist(TemperatureSensor record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}

}
