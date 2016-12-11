package com.gdut.dongjun.service.device.current.impl;

import com.gdut.dongjun.domain.dao.ControlMearsureCurrentMapper;
import com.gdut.dongjun.domain.po.ControlMearsureCurrent;
import com.gdut.dongjun.service.device.current.ControlMearsureCurrentService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ControlMearsureCurrentServiceImpl extends CurrentServiceImpl<ControlMearsureCurrent> implements
		ControlMearsureCurrentService {

	@Autowired
	private ControlMearsureCurrentMapper currentMapper;

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
	public List<ControlMearsureCurrent> getRecentlyCurrent() {
		// TODO Auto-generated method stub
		return currentMapper.getRecentlyCurrent();
	}

	@Override
	protected boolean isExist(ControlMearsureCurrent record) {

		if (record != null
				&& currentMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Integer> readCurrent(String switchId) {
		throw new UnsupportedOperationException();
	}
}
