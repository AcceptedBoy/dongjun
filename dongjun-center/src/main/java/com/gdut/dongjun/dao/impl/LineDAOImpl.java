package com.gdut.dongjun.dao.impl;

import org.springframework.stereotype.Repository;

import com.gdut.dongjun.dao.LineMapper;
import com.gdut.dongjun.dao.impl.SinglePrimaryKeyBaseDAOImpl;
import com.gdut.dongjun.po.Line;

@Repository
public class LineDAOImpl extends SinglePrimaryKeyBaseDAOImpl<Line> implements LineMapper {

}