package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModuleVoltageMapper;
import com.gdut.dongjun.domain.po.ElectronicModuleVoltage;
import com.gdut.dongjun.service.ElectronicModuleVoltageService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class ElectronicModuleVoltageServiceImpl extends BaseServiceImpl<ElectronicModuleVoltage> implements ElectronicModuleVoltageService {

	@Autowired
	private ElectronicModuleVoltageMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModuleVoltage record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
 }