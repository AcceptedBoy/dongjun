package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.SubstationMapper;
import com.gdut.dongjun.po.Substation;
import com.gdut.dongjun.service.SubstationService;

@Service
public class SubstationServiceImpl extends BaseServiceImpl<Substation> implements SubstationService {
 
	@Autowired
	private SubstationMapper mapper;
	
	@Override
	protected boolean isExist(Substation record) {
		if (null != record && null != record.getId()
				&& null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}
}