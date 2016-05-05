package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.HighSwitchUserMapper;
import com.gdut.dongjun.domain.po.HighSwitchUser;
import com.gdut.dongjun.service.HighSwitchUserService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;

@Service
public class HighSwitchUserServiceImpl extends BaseServiceImpl<HighSwitchUser> implements
	HighSwitchUserService {
	
	@Autowired
	private HighSwitchUserMapper switchUserMapper;

	@Override
	protected boolean isExist(HighSwitchUser record) {

		if (record != null
				&& switchUserMapper.selectByPrimaryKey(record.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	
	
}
