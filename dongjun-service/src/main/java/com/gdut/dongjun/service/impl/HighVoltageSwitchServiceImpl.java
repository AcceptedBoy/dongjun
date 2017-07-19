/**
 * 
 */
package com.gdut.dongjun.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.HighVoltageSwitchMapper;
import com.gdut.dongjun.po.HighVoltageSwitch;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;


/**
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月21日
 * @description
 * @package com.gdut.dongjun.service.impl
 */
@Service
public class HighVoltageSwitchServiceImpl extends
		BaseServiceImpl<HighVoltageSwitch> implements HighVoltageSwitchService {

	@Autowired
	private HighVoltageSwitchMapper currentMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdut.dongjun.service.HighVoltageSwitchService#delSwitchByLineId(java
	 * .lang.String)
	 */
	@Override
	public void delSwitchByLineId(String lineId) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdut.dongjun.service.HighVoltageSwitchService#updateSwitch(com.gdut
	 * .dongjun.domain.po.HighVoltageSwitch)
	 */
	@Override
	public void updateSwitch(HighVoltageSwitch switch1) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdut.dongjun.service.HighVoltageSwitchService#isSwitchExist(com.gdut
	 * .dongjun.domain.po.HighVoltageSwitch)
	 */
	@Override
	public boolean isSwitchExist(HighVoltageSwitch switch1) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdut.dongjun.service.HighVoltageSwitchService#getAddress(java.lang
	 * .String)
	 */
	@Override
	public String getAddress(String switchId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdut.dongjun.service.HighVoltageSwitchService#selectByLineId(java
	 * .lang.String)
	 */
	@Override
	public List<HighVoltageSwitch> selectByLineId(String lineId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdut.dongjun.service.HighVoltageSwitchService#getSwitchId(java.lang
	 * .String)
	 */
	@Override
	public String getSwitchId(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isExist(HighVoltageSwitch record) {

		if (record != null
				&& currentMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> selectIdByCompanyId(String companyId) {
		return currentMapper.selectIdByCompanyId(companyId);
	}

	@Override
	public List<String> selectIdByLineIds(List<String> lineIds) {
		return currentMapper.selectIdByLineIds(lineIds);
	}

	@Override
	public int deleteByIds(List<String> list) {
		return currentMapper.deleteByIds(list);
	}

	@Override
	public List<String> selectAddrAvailableByCompanyId(String companyId) {
		return currentMapper.selectAddrAvailableByCompanyId(companyId);
	}
}
