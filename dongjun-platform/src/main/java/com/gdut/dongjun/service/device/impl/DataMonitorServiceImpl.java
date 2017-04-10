package com.gdut.dongjun.service.device.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.DataMonitorMapper;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.service.device.DataMonitorService;

@Service
public class DataMonitorServiceImpl extends BaseServiceImpl<DataMonitor> 
implements DataMonitorService {

	@Autowired
	private DataMonitorMapper mapper;
	
	@Override
	protected boolean isExist(DataMonitor record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}

}
