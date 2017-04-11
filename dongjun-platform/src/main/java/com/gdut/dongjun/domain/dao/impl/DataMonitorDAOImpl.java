package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.DataMonitorMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.DataMonitor;

@Repository
public class DataMonitorDAOImpl extends
SinglePrimaryKeyBaseDAOImpl<DataMonitor>
implements DataMonitorMapper {

}
