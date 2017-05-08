package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ModuleHitchEventMapper;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.service.ModuleHitchEventService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;

@Service
public class ModuleHitchEventServiceImpl extends EnhancedServiceImpl<ModuleHitchEvent> implements ModuleHitchEventService {

	@Autowired
	private ModuleHitchEventMapper mapper;
	
	@Override
	protected boolean isExist(ModuleHitchEvent record) {
		if (record != null && null != record.getId() &&
				mapper.selectByPrimaryKey(record.getId()) != null)
			return true;
		return false;
	}
 }