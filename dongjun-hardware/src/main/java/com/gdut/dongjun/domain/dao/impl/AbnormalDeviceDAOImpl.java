package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.AbnormalDeviceMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.AbnormalDevice;

@Repository
public class AbnormalDeviceDAOImpl extends SinglePrimaryKeyBaseDAOImpl<AbnormalDevice> 
implements AbnormalDeviceMapper {

}
