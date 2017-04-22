package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.UserLog;
import com.gdut.dongjun.service.base.EnhancedService;

public interface UserLogService extends EnhancedService<UserLog> {
	
	public void createNewLog(User user);

}
