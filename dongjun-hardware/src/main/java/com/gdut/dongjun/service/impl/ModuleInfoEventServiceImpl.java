package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ModuleInfoEventMapper;
import com.gdut.dongjun.domain.po.ModuleInfoEvent;
import com.gdut.dongjun.service.ModuleInfoEventService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class ModuleInfoEventServiceImpl extends BaseServiceImpl<ModuleInfoEvent> implements ModuleInfoEventService {

	@Autowired
	private ModuleInfoEventMapper mapper;
	
	@Override
	protected boolean isExist(ModuleInfoEvent record) {
		if (record != null && null != record.getId() &&
				mapper.selectByPrimaryKey(record.getId()) != null)
			return true;
		return false;
	}
 }