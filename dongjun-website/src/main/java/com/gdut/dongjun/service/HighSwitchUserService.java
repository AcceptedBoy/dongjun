package com.gdut.dongjun.service;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.po.HighSwitchUser;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;
import com.gdut.dongjun.service.base.BaseService;

public interface HighSwitchUserService extends
	BaseService<HighSwitchUser> {

	public List<HighVoltageSwitch> getSwitchByUserId(String userId);

	//public boolean deleteByParameters(Map<String, Object> map);

}
