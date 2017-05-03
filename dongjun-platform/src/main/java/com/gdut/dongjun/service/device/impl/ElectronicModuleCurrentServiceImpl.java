package com.gdut.dongjun.service.device.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModuleCurrentMapper;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.service.device.ElectronicModuleCurrentService;

@Service
public class ElectronicModuleCurrentServiceImpl extends EnhancedServiceImpl<ElectronicModuleCurrent> implements ElectronicModuleCurrentService {

	@Autowired
	private ElectronicModuleCurrentMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModuleCurrent record) {
		if (record != null && null != record.getId()
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
 }