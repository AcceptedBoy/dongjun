package com.gdut.dongjun.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.UserLog;
import com.gdut.dongjun.service.UserLogService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;
import com.gdut.dongjun.util.UUIDUtil;

@Service
public class UserLogServiceImpl extends 
BaseServiceImpl<UserLog> implements UserLogService {

	@Override
	protected boolean isExist(UserLog record) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createNewLog(User user) {
		UserLog log = new UserLog();
		log.setId(UUIDUtil.getUUID());
		log.setUserId(user.getId());
		log.setType(1);
		log.setDate(new Date());
		this.insert(log);
	}

}
