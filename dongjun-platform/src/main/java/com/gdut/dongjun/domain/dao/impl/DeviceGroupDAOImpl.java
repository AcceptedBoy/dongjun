package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.DeviceGroupMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.DeviceGroup;

@Repository
public class DeviceGroupDAOImpl extends SinglePrimaryKeyBaseDAOImpl<DeviceGroup> implements DeviceGroupMapper {

}
