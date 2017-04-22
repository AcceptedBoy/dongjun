package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.BigGroupMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.BigGroup;

/**
 * Created by symon on 16-10-18.
 */
@Repository
public class BigGroupDAOImpl extends SinglePrimaryKeyBaseDAOImpl<BigGroup>
    implements BigGroupMapper {
}
