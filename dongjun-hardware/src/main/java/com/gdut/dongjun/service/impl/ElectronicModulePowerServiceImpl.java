package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModulePowerMapper;
import com.gdut.dongjun.domain.po.ElectronicModulePower;
import com.gdut.dongjun.service.ElectronicModulePowerService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class ElectronicModulePowerServiceImpl extends EnhancedServiceImpl<ElectronicModulePower>
		implements ElectronicModulePowerService {

	@Autowired
	private ElectronicModulePowerMapper mapper;

	@Override
	protected boolean isExist(ElectronicModulePower record) {
		if (record != null && (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
}