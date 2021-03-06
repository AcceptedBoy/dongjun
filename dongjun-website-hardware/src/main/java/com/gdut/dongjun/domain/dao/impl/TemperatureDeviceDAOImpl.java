package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.TemperatureDeviceMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.TemperatureDevice;

@Repository
public class TemperatureDeviceDAOImpl extends SinglePrimaryKeyBaseDAOImpl<TemperatureDevice> 
		implements TemperatureDeviceMapper {

}