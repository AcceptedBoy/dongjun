package com.gdut.dongjun.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.ModuleMapper;
import com.gdut.dongjun.dao.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.Module;

@Repository
public class ModuleDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Module> implements ModuleMapper {

}