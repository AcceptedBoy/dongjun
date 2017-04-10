package com.gdut.dongjun.service.device.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureModuleMapper;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.service.device.TemperatureModuleService;

@Service
public class TemperatureModuleServiceImpl extends BaseServiceImpl<TemperatureModule> 
implements TemperatureModuleService {

	@Autowired
	private TemperatureModuleMapper mapper;
	
	@Override
	protected boolean isExist(TemperatureModule record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}

}
