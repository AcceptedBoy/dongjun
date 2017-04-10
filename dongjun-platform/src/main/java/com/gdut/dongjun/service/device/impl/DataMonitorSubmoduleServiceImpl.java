package com.gdut.dongjun.service.device.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.DataMonitorSubmoduleMapper;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.service.device.DataMonitorSubmoduleService;

@Service
public class DataMonitorSubmoduleServiceImpl extends BaseServiceImpl<DataMonitorSubmodule> 
implements DataMonitorSubmoduleService {

	@Autowired
	private DataMonitorSubmoduleMapper mapper;
	
	@Override
	protected boolean isExist(DataMonitorSubmodule record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}


}
