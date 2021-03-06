/**
 * 
 */
package com.gdut.dongjun.service.device.current.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdut.dongjun.service.device.current.HighVoltageCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.HighVoltageCurrentMapper;
import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 * @author zjd
 * @email 452880294@qq.com
 * @date 2015年11月21日
 * @description
 * @package com.gdut.dongjun.service.impl
 */
@Service
public class HighVoltageCurrentServiceImpl extends CurrentServiceImpl<HighVoltageCurrent> implements
		HighVoltageCurrentService {

	@Autowired
	private HighVoltageCurrentMapper currentMapper;
	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.HighVoltageCurrentService#selectBySwitchId(java.lang.String)
	 */
	@Override
	public Map<String, Object> selectBySwitchId(String switchId) {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("switch_id", switchId);
		map.put("phase", "A");
		result.put("A",
				currentMapper.selectBySwitchId(MyBatisMapUtil.warp(map)));
		map.remove("phase");
		map.put("phase", "B");
		result.put("B",
				currentMapper.selectBySwitchId(MyBatisMapUtil.warp(map)));
		map.remove("phase");
		map.put("phase", "C");
		result.put("C",
				currentMapper.selectBySwitchId(MyBatisMapUtil.warp(map)));
		// System.out.println(map.get("A")[0]);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.HighVoltageCurrentService#selectByTime(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Object> selectByTime(String switchId, String beginDate,
			String endDate) {

		/*Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> xx = MapUtil.warp("switchId", switchId);
		xx.put("phase", "A");
		xx.put("beginDate", beginDate);
		xx.put("endDate", endDate);
		result.put("A", currentMapper.selectByTime(xx));
		xx.remove("phase");
		xx.put("phase", "B");
		result.put("B", currentMapper.selectByTime(xx));
		xx.remove("phase");
		xx.put("phase", "C");
		result.put("C", currentMapper.selectByTime(xx));
		return result;*/
		Map<String, Object> xx = MapUtil.warp("switchId", switchId);
		xx.put("beginDate", beginDate);
		xx.put("endDate", endDate);
		return currentMapper.selectByTime(xx);
	}

	/* (non-Javadoc)
	 * @see com.gdut.dongjun.service.HighVoltageCurrentService#getRecentlyCurrent()
	 */
	@Override
	public List<HighVoltageCurrent> getRecentlyCurrent(String switchId, String phase) {
		// TODO Auto-generated method stub
		HighVoltageCurrent hc  = new HighVoltageCurrent();
		hc.setSwitchId(switchId);
		hc.setPhase(phase);
		return currentMapper.getRecentlyCurrent(hc);
	}

	@Override
	protected boolean isExist(HighVoltageCurrent record) {

		if (record != null
				&& currentMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Integer> readCurrent(String switchId) {
		// 从数据库中查询结果
		List<Integer> result = new ArrayList<>(4);
		List<HighVoltageCurrent> cliList2 = getRecentlyCurrent(switchId, "A");
		if(cliList2 != null && cliList2.size() != 0) {
			result.add(cliList2.get(0).getValue());
		}
		cliList2 = getRecentlyCurrent(switchId, "B");
		if(cliList2 != null && cliList2.size() != 0) {
			result.add(cliList2.get(0).getValue());
		}
		cliList2 = getRecentlyCurrent(switchId, "C");
		if(cliList2 != null && cliList2.size() != 0) {
			result.add(cliList2.get(0).getValue());
		}
		return result;
	}
}
