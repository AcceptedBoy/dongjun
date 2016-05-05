package com.gdut.dongjun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.LowVoltageHitchEventMapper;
import com.gdut.dongjun.domain.po.LowVoltageHitchEvent;
import com.gdut.dongjun.service.LowVoltageHitchEventService;
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
public class LowVoltageHitchEventServiceImpl extends
		BaseServiceImpl<LowVoltageHitchEvent> implements
		LowVoltageHitchEventService {
	/**
	 * @ClassName: UserServiceImpl
	 * @Description: TODO
	 * @author Sherlock-lee
	 * @date 2015年7月24日 下午2:33:08
	 */
	@Autowired
	private LowVoltageHitchEventMapper hitchEventMapper;

	@Override
	public LowVoltageHitchEvent getRecentlyHitchEvent() {
		// TODO Auto-generated method stub
		return hitchEventMapper.getRecentlyHitchEvent();
	}


	@Override
	protected boolean isExist(LowVoltageHitchEvent record) {

		if (record != null
				&& hitchEventMapper.selectByPrimaryKey(record.getId()) != null) {

			return true;
		} else {
			return false;
		}
	}

}
