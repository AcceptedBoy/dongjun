package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.OperationLogMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.OperationLog;

@Repository
public class OperationLogDAOImpl extends SinglePrimaryKeyBaseDAOImpl<OperationLog> implements
OperationLogMapper {

}
