package com.gdut.dongjun.domain.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.HighSwitchUserMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.HighSwitchUser;
import com.gdut.dongjun.domain.po.HighVoltageSwitch;

@Repository
public class HighSwitchUserDaoImpl extends SinglePrimaryKeyBaseDAOImpl<HighSwitchUser>
	implements HighSwitchUserMapper{

	@Override
	public List<HighVoltageSwitch> getSwitchByUserId(String userId) {
		
		return template.selectList(getNamespace("getSwitchByUserId"), userId);
	}

	@Override
	public boolean deleteByParameters(Map<String, Object> map) {
		
		if(template.delete(getNamespace("deleteByParameters"), map) == 1) {
			return true;
		}
		return false;
	}
}
