package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModuleCurrentMapper;
import com.gdut.dongjun.domain.po.ElectronicModuleCurrent;
import com.gdut.dongjun.service.ElectronicModuleCurrentService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class ElectronicModuleCurrentServiceImpl extends EnhancedServiceImpl<ElectronicModuleCurrent> implements ElectronicModuleCurrentService {

	@Autowired
	private ElectronicModuleCurrentMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModuleCurrent record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
 }