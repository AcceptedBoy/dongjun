package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.UserLogMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.UserLog;

@Repository
public class UserLogDAOImpl extends SinglePrimaryKeyBaseDAOImpl<UserLog> implements
UserLogMapper {

}
