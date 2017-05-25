package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.LineMapper;
import com.gdut.dongjun.po.Line;
import com.gdut.dongjun.service.LineService;

@Service
public class LineServiceImpl extends BaseServiceImpl<Line> implements LineService {
 
	@Autowired
	private LineMapper mapper;
	
	@Override
	protected boolean isExist(Line record) {
		if (null != record && null != record.getId()
				&& null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}
}