package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.TemperatureSignalHistoryMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.TemperatureSignalHistory;

@Repository
public class TemperatureSignalHistoryDAOImpl extends SinglePrimaryKeyBaseDAOImpl<TemperatureSignalHistory>
		implements TemperatureSignalHistoryMapper{

}
