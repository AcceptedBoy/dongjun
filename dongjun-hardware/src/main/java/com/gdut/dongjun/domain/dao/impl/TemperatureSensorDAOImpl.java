package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.TemperatureSensorMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.TemperatureSensor;

@Repository
public class TemperatureSensorDAOImpl extends SinglePrimaryKeyBaseDAOImpl<TemperatureSensor> implements TemperatureSensorMapper {

}
