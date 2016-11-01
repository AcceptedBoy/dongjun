package com.gdut.dongjun.domain.dao;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.base.SinglePrimaryKeyBaseMapper;
import com.gdut.dongjun.domain.po.TemperatureSignal;

@Repository
public interface TemperatureSignalMapper 
		extends SinglePrimaryKeyBaseMapper<TemperatureSignal> {

}
