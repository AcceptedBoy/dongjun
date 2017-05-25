package com.gdut.dongjun.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.LowVoltageSwitchMapper;
import com.gdut.dongjun.dao.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.LowVoltageSwitch;

@Repository
public class LowVoltageSwitchDAOImpl extends SinglePrimaryKeyBaseDAOImpl<LowVoltageSwitch> implements LowVoltageSwitchMapper {

}