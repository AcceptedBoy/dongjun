package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.groupMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.group;

/**
 * Created by symon on 16-10-18.
 */
@Repository
public class groupDAOImpl extends SinglePrimaryKeyBaseDAOImpl<group>
    implements groupMapper {
}
