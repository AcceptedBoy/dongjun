package com.gdut.dongjun.service.impl;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.AbnormalDevice;
import com.gdut.dongjun.service.AbnormalDeviceService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class AbnormalDeviceServiceImpl extends
		BaseServiceImpl<AbnormalDevice> implements AbnormalDeviceService{

	@Override
	protected boolean isExist(AbnormalDevice record) {
		// TODO Auto-generated method stub
		return false;
	}

}
