package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.ElectronicModuleMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.ElectronicModule;

@Repository
public class ElectronicModuleDAOImpl extends SinglePrimaryKeyBaseDAOImpl<ElectronicModule> implements ElectronicModuleMapper {

}