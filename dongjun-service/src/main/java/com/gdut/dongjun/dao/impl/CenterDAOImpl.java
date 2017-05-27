package com.gdut.dongjun.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.CenterMapper;
import com.gdut.dongjun.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.Center;

/**
 * Created by symon on 16-9-28.
 */
@Repository
public class CenterDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Center>
    implements CenterMapper {
}
