package com.gdut.dongjun.service.device.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModuleVoltageMapper;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.device.ElectronicModuleVoltageService;

@Service
public class ElectronicModuleVoltageServiceImpl extends EnhancedServiceImpl<ElectronicModuleVoltage> implements ElectronicModuleVoltageService {
 
	@Autowired
	private ElectronicModuleVoltageMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModuleVoltage record) {
		if (record != null && null != record.getId()
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
	
}