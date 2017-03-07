package com.gdut.dongjun.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.OperationLogMapper;
import com.gdut.dongjun.domain.po.OperationLog;
import com.gdut.dongjun.service.OperationLogService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.util.UUIDUtil;

@Service
public class OperationLogServiceImpl extends 
BaseServiceImpl<OperationLog> implements OperationLogService{

	@Autowired
	private OperationLogMapper mapper;
	
	@Override
	protected boolean isExist(OperationLog record) {
		if (record != null
				&& mapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void createNewOperationLog(String userId, int type, String switchId, String companyId) {
		OperationLog log = new OperationLog();
		log.setId(UUIDUtil.getUUID());
		log.setDate(new Date());
		log.setSwitchId(switchId);
		log.setType(type);
		log.setUserId(userId);
		log.setCompanyId(companyId);
		this.insert(log);
	}

}
