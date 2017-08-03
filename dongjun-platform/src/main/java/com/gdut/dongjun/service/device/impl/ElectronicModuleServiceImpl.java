package com.gdut.dongjun.service.device.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ElectronicModuleMapper;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.service.base.impl.DelTagHolderServiceImpl;
import com.gdut.dongjun.service.device.ElectronicModuleService;

@Service
public class ElectronicModuleServiceImpl extends DelTagHolderServiceImpl<ElectronicModule> 
implements ElectronicModuleService {

	@Autowired
	private ElectronicModuleMapper mapper;
	
	@Override
	protected boolean isExist(ElectronicModule record) {
		if (record != null 
				&& (mapper.selectByPrimaryKey(record.getId()) != null))
			return true;
		return false;
	}
	
}