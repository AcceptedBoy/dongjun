package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModuleHitchEventMapper;
import com.gdut.dongjun.domain.po.ElectronicModuleHitchEvent;
import com.gdut.dongjun.service.ElectronicModuleHitchEventService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class ElectronicModuleHitchEventServiceImpl extends EnhancedServiceImpl<ElectronicModuleHitchEvent> implements ElectronicModuleHitchEventService {

	@Autowired
	private ElectronicModuleHitchEventMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModuleHitchEvent record) {
		if (record != null && null != record.getId()
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
 }