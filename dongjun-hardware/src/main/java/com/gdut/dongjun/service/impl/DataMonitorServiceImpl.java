package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.DataMonitorMapper;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.service.DataMonitorService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class DataMonitorServiceImpl extends BaseServiceImpl<DataMonitor> implements DataMonitorService {

	@Autowired
	private DataMonitorMapper mapper;
	
	@Override
	protected boolean isExist(DataMonitor record) {
		if (null != record.getId() &&
				null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}
 }