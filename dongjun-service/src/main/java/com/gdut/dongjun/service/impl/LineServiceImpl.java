package com.gdut.dongjun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.dao.LineMapper;
import com.gdut.dongjun.po.Line;
import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.service.base.impl.BaseServiceImpl;


/**
 * @Title: UserServiceImpl.java
 * @Package com.gdut.dongjun.service.impl.system
 * @Description: TODO
 * @author Sherlock-lee
 * @date 2015年7月24日 下午2:33:08
 * @version V1.0
 */
@Service
public class LineServiceImpl extends BaseServiceImpl<Line> implements
		LineService {
	/**
	 * @ClassName: UserServiceImpl
	 * @Description: TODO
	 * @author Sherlock-lee
	 * @date 2015年7月24日 下午2:33:08
	 */
	@Autowired
	private LineMapper lineMapper;


	@Override
	protected boolean isExist(Line record) {
		
		if (record != null
				&& lineMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}


	@Override
	public List<String> selectIdBySubstationIds(List<String> list) {
		return lineMapper.selectIdBySubstationIds(list);
	}


	@Override
	public int deleteByIds(List<String> list) {
		return lineMapper.deleteByIds(list);
	}

}
