package com.gdut.dongjun.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.SubstationMapper;
import com.gdut.dongjun.dao.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.Substation;

@Repository
public class SubstationDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Substation> implements SubstationMapper {

}