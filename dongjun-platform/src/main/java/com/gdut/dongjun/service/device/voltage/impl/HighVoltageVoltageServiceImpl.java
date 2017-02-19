/**
 * 
 */
package com.gdut.dongjun.service.device.voltage.impl;

import com.gdut.dongjun.domain.dao.HighVoltageVoltageMapper;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.service.device.voltage.HighVoltageVoltageService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月21日
 * @description
 * @package com.gdut.dongjun.service.impl
 */
@Service
public class HighVoltageVoltageServiceImpl extends DeviceVoltageServiceImpl<HighVoltageVoltage> implements
		HighVoltageVoltageService {

	@Autowired
	private HighVoltageVoltageMapper voltageMapper;
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
		
		//List<HighVoltageVoltage> result = new ArrayList<HighVoltageVoltage>();
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
	public List<HighVoltageVoltage> getRecentlyVoltage(String switchId, String phase) {
		// TODO Auto-generated method stub
		HighVoltageVoltage hv = new HighVoltageVoltage();
		hv.setSwitchId(switchId);
		hv.setPhase(phase);
		return voltageMapper.getRecentlyVoltage(hv);
	}

	@Override
	protected boolean isExist(HighVoltageVoltage record) {

		if (record != null
				&& voltageMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}


	@Override
	public List<Integer> getVoltage(String switchId) {
		List<Integer> result = new ArrayList<>();
		List<HighVoltageVoltage> cliList2 = getRecentlyVoltage(switchId, "A");
		if (cliList2 != null && cliList2.size() != 0) {
			result.add(cliList2.get(0).getValue());
		} else {
			result.add(0); //0
		}
		cliList2 = getRecentlyVoltage(switchId, "B");
		if (cliList2 != null && cliList2.size() != 0) {
			result.add(cliList2.get(0).getValue());
		} else {
			result.add(0); //0
		}
		cliList2 = getRecentlyVoltage(switchId, "C");
		if (cliList2 != null && cliList2.size() != 0) {
			result.add(cliList2.get(0).getValue());
		} else {
			result.add(0); //0
		}
		return result;
	}
}
