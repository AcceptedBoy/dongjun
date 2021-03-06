package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModuleMapper;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.service.ElectronicModuleService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class ElectronicModuleServiceImpl extends EnhancedServiceImpl<ElectronicModule> implements ElectronicModuleService {
 
	@Autowired
	private ElectronicModuleMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModule record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
	
	@Override
	public ElectronicModule selectByDeviceNumber(String deviceNumber) {
		return mapper.selectByDeviceNumber(deviceNumber);
	}
}