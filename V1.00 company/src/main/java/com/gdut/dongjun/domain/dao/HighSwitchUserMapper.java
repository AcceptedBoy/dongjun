package com.gdut.dongjun.domain.dao;

import java.util.List;
import java.util.Map;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.HighSwitchUser;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;

public interface HighSwitchUserMapper 
	extends SinglePrimaryKeyBaseMapper<HighSwitchUser>{

	public List<HighVoltageSwitch> getSwitchByUserId(String userId);

	public boolean deleteByParameters(Map<String, Object> map);

}
