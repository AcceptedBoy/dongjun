package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.GPRSModuleMapper;
import com.gdut.dongjun.domain.po.GPRSModule;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class GPRSModuleServiceImpl extends BaseServiceImpl<GPRSModule> implements
GPRSModuleService {

	@Autowired
	private GPRSModuleMapper mapper;
	
	@Override
	protected boolean isExist(GPRSModule record) {
		if (null != record
				&& null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}

}
