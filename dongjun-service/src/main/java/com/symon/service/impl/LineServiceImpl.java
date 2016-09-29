package com.symon.service.impl;

import com.symon.dao.LineMapper;
import com.symon.po.Line;
import com.symon.service.LineService;
import com.symon.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

}
