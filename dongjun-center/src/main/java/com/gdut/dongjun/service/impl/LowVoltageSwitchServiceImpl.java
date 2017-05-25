package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.LowVoltageSwitchMapper;
import com.gdut.dongjun.po.LowVoltageSwitch;
import com.gdut.dongjun.service.LowVoltageSwitchService;

@Service
public class LowVoltageSwitchServiceImpl extends BaseServiceImpl<LowVoltageSwitch> implements LowVoltageSwitchService {

	@Autowired
	private LowVoltageSwitchMapper mapper;
	
	@Override
	protected boolean isExist(LowVoltageSwitch record) {
		if (null != record && null != record.getId()
				&& null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}
}