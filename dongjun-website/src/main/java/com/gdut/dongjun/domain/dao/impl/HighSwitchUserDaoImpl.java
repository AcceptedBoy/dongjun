package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.HighSwitchUserMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.HighSwitchUser;

@Repository
public class HighSwitchUserDaoImpl extends SinglePrimaryKeyBaseDAOImpl<HighSwitchUser>
	implements HighSwitchUserMapper{

}
