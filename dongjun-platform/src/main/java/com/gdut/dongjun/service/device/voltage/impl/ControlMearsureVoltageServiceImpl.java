package com.gdut.dongjun.service.device.voltage.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.ControlMearsureVoltageMapper;
import com.gdut.dongjun.domain.po.ControlMearsureVoltage;
import com.gdut.dongjun.service.device.voltage.ControlMearsureVoltageService;
import com.gdut.dongjun.util.MyBatisMapUtil;


@Service
public class ControlMearsureVoltageServiceImpl extends
        DeviceVoltageServiceImpl<ControlMearsureVoltage> implements
		ControlMearsureVoltageService {

	@Autowired
	private ControlMearsureVoltageMapper voltageMapper;
	
	@Override
	public Map<String, Object> selectBySwitchId(String switchId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("switch_id", switchId);
		map.put("phase", "A");
		result.put("A", voltageMapper.selectBySwitchId(MyBatisMapUtil.warp(map)));
		map.remove("phase");
		map.put("phase", "B");
		result.put("B", voltageMapper.selectBySwitchId(MyBatisMapUtil.warp(map)));
		map.remove("phase");
		map.put("phase", "C");
		result.put("C", voltageMapper.selectBySwitchId(MyBatisMapUtil.warp(map)));
		return result;
	}

	@Override
	public List<Object> selectByTime(String switchId, String beginDate,
			String endDate) {
		
		//Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> xx = new HashMap<String, Object>();
		xx.put("switchId", switchId);
		//xx.put("phase", "A");
		xx.put("beginDate", beginDate);
		xx.put("endDate", endDate);
		/*result.put("A", voltageMapper.selectByTime(xx));
		xx.remove("phase");
		xx.put("phase", "B");
		result.put("B", voltageMapper.selectByTime(xx));
		xx.remove("phase");
		xx.put("phase", "C");
		result.put("C", voltageMapper.selectByTime(xx));*/
		return voltageMapper.selectByTime(xx);
	}
	
	@Override
	public List<ControlMearsureVoltage> getRecentlyVoltage() {
		return voltageMapper.getRecentlyVoltage();
	}

	@Override
	protected boolean isExist(ControlMearsureVoltage record) {

		if (record != null
				&& voltageMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Integer> getVoltage(String switchId) {
		throw new UnsupportedOperationException();
	}
}
