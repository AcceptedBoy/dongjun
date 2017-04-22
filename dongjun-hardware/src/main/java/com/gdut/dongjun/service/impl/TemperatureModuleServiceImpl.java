package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.TemperatureModuleMapper;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.TemperatureModuleService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class TemperatureModuleServiceImpl extends EnhancedServiceImpl<TemperatureModule>
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
