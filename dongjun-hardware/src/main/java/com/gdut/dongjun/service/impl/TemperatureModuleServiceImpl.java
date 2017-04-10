package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.domain.dao.TemperatureModuleMapper;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.TemperatureModuleService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

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
