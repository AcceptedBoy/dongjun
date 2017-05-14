package com.gdut.dongjun.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.domain.dao.ModuleInfoEventMapper;
import com.gdut.dongjun.domain.dao.base.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.domain.po.ModuleInfoEvent;

@Repository
public class ModuleInfoEventDAOImpl extends SinglePrimaryKeyBaseDAOImpl<ModuleInfoEvent> implements ModuleInfoEventMapper {

}