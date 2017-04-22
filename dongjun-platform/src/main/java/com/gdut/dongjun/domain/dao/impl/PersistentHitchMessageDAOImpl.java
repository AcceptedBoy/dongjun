package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.PersistentHitchMessageMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.PersistentHitchMessage;

@Repository
public class PersistentHitchMessageDAOImpl extends SinglePrimaryKeyBaseDAOImpl<PersistentHitchMessage>
		implements PersistentHitchMessageMapper {

}
