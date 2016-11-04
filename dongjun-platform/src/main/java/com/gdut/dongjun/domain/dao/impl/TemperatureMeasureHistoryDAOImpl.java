package com.gdut.dongjun.domain.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.TemperatureMeasureHistoryMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;

@Repository
public class TemperatureMeasureHistoryDAOImpl extends SinglePrimaryKeyBaseDAOImpl<TemperatureMeasureHistory>
		implements TemperatureMeasureHistoryMapper{

	@Override
	public List<TemperatureMeasureHistory> selectByTime(Map<String, Object> xx) {
		return template.selectList(getNamespace("selectByTime"), xx);
	}


}
