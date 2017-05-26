package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.HighVoltageSwitchMapper;
import com.gdut.dongjun.po.HighVoltageSwitch;
import com.gdut.dongjun.service.HighVoltageSwitchService;

@Service
public class HighVoltageSwitchServiceImpl extends BaseServiceImpl<HighVoltageSwitch> implements HighVoltageSwitchService {
 
	@Autowired
	private HighVoltageSwitchMapper mapper;
	
	@Override
	protected boolean isExist(HighVoltageSwitch record) {
		if (null != record && null != record.getId()
				&& null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}
}