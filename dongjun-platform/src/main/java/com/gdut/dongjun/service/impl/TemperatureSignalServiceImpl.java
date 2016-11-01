package com.gdut.dongjun.service.impl;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.TemperatureSignal;
import com.gdut.dongjun.service.TemperatureSignalService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class TemperatureSignalServiceImpl extends BaseServiceImpl<TemperatureSignal>
		implements TemperatureSignalService {

	@Override
	protected boolean isExist(TemperatureSignal record) {
		// TODO Auto-generated method stub
		return false;
	}

}
