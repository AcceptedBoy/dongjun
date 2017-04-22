package com.gdut.dongjun.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.OperationLog;
import com.gdut.dongjun.service.OperationLogService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.util.UUIDUtil;

@Service
public class OperationLogServiceImpl extends 
BaseServiceImpl<OperationLog> implements OperationLogService{

	@Override
	protected boolean isExist(OperationLog record) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createNewOperationLog(String userId, int type, String switchId) {
		OperationLog log = new OperationLog();
		log.setId(UUIDUtil.getUUID());
		log.setDate(new Date());
		log.setSwitchId(switchId);
		log.setType(type);
		log.setUserId(userId);
		this.insert(log);
	}

}
