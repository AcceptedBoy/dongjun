package com.gdut.dongjun.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.HighVoltageSwitchMapper;
import com.gdut.dongjun.dao.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.HighVoltageSwitch;

@Repository
public class HighVoltageSwitchDAOImpl extends SinglePrimaryKeyBaseDAOImpl<HighVoltageSwitch> implements HighVoltageSwitchMapper {

}