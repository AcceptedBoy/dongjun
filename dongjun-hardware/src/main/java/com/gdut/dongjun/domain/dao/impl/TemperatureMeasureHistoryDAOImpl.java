package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.TemperatureMeasureHistoryMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.TemperatureMeasureHistory;

@Repository
public class TemperatureMeasureHistoryDAOImpl extends SinglePrimaryKeyBaseDAOImpl<TemperatureMeasureHistory>
		implements TemperatureMeasureHistoryMapper{

}
