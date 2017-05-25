package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.ModuleMapper;
import com.gdut.dongjun.po.Module;
import com.gdut.dongjun.service.ModuleService;

@Service
public class ModuleServiceImpl extends BaseServiceImpl<Module> implements ModuleService {
 
	@Autowired
	private ModuleMapper mapper;
	
	@Override
	protected boolean isExist(Module record) {
		if (null != record && null != record.getId()
				&& null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}
}