package com.gdut.dongjun.domain.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.TemperatureMeasureMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.TemperatureMeasure;

@Repository
public class TemperatureMeasureDAOImpl extends SinglePrimaryKeyBaseDAOImpl<TemperatureMeasure>
		implements TemperatureMeasureMapper {
	
	@Override
	public List<TemperatureMeasure> selectByTimeAndSensorId(Map<String, Object> xx) {
		return template.selectList(getNamespace("selectByTimeAndSensorId"), xx);
	}

}
