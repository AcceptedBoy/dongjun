package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.DataMonitorSubmoduleMapper;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.service.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class DataMonitorSubmoduleServiceImpl extends BaseServiceImpl<DataMonitorSubmodule> implements DataMonitorSubmoduleService {

	@Autowired
	private DataMonitorSubmoduleMapper mapper;
	
	@Override
	protected boolean isExist(DataMonitorSubmodule record) {
		if (record != null && null != record.getId()
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
 }