package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.UserMapper;
import com.gdut.dongjun.po.User;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class UserServiceImpl extends BaseServiceImpl<User>
implements UserService {

	@Autowired
	private UserMapper mapper;
	
	@Override
	protected boolean isExist(User record) {
		if (null != record && null != record.getId() &&
				null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}

}
