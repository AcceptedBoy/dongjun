package com.gdut.dongjun.service.device.current.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.LowVoltageCurrentMapper;
import com.gdut.dongjun.domain.po.LowVoltageCurrent;
import com.gdut.dongjun.service.device.current.LowVoltageCurrentService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 * @Title: UserServiceImpl.java
 * @Package com.gdut.dongjun.service.impl.system
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年7月24日 下午2:33:08
 * @version V1.0
 */
@Service
public class LowVoltageCurrentServiceImpl extends CurrentServiceImpl<LowVoltageCurrent> implements
		LowVoltageCurrentService {
	/**
	 * @ClassName: UserServiceImpl
	 * @Description: TODO
	 * @author Sherlock-lee
	 * @date 2015年7月24日 下午2:33:08
	 */
	@Autowired
	private LowVoltageCurrentMapper currentMapper;

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

	@Override
	public List<Object> selectByTime(String switchId, String beginDate,
			String endDate) {

		//Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> xx = MapUtil.warp("switchId", switchId);
		//xx.put("phase", "A");
		xx.put("beginDate", beginDate);
		xx.put("endDate", endDate);
		/*result.put("A", currentMapper.selectByTime(xx));
		xx.remove("phase");
		xx.put("phase", "B");
		result.put("B", currentMapper.selectByTime(xx));
		xx.remove("phase");
		xx.put("phase", "C");
		result.put("C", currentMapper.selectByTime(xx));*/
		return currentMapper.selectByTime(xx);
	}

	@Override
	public List<LowVoltageCurrent> getRecentlyCurrent() {
		// TODO Auto-generated method stub
		return currentMapper.getRecentlyCurrent();
	}

	@Override
	protected boolean isExist(LowVoltageCurrent record) {

		if (record != null
				&& currentMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Integer> readCurrent(String switchId) {
		Integer[] deStrings = new Integer[3];
		List<LowVoltageCurrent> cliList = getRecentlyCurrent();

		if (cliList != null) {
			for (LowVoltageCurrent current : cliList) {
				String p = current.getPhase();
				switch (p) {
					case "A":
						deStrings[0] = current.getValue();
						break;
					case "B":
						deStrings[1] = current.getValue();
						break;
					case "C":
						deStrings[2] = current.getValue();
						break;
					default:
						break;
				}
			}
		}
		return Arrays.asList(deStrings);
	}
}
