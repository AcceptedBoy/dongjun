package com.gdut.dongjun.domain.dao.impl;

import com.gdut.dongjun.domain.po.ProtocolPort;
import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.ProtocolPortMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;

@Repository
public class ProtocolPortDAOImpl extends SinglePrimaryKeyBaseDAOImpl<ProtocolPort>
	implements ProtocolPortMapper {

}
