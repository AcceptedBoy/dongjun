package com.gdut.dongjun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdut.dongjun.domain.dao.PersistentHitchMessageMapper;
import com.gdut.dongjun.domain.po.PersistentHitchMessage;
import com.gdut.dongjun.service.PersistentHitchMessageService;
import com.gdut.dongjun.service.base.impl.EnhancedServiceImpl;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Service
public class PersistentHitchMessageServiceImpl extends EnhancedServiceImpl<PersistentHitchMessage>
		implements PersistentHitchMessageService {

	@Autowired
	private PersistentHitchMessageMapper mapper;
	
	@Override
	protected boolean isExist(PersistentHitchMessage record) {
		if (null != record &&
				null != mapper.selectByPrimaryKey(record.getId())) {
			return true;
		}
		return false;
	}

	@Override
	public List<PersistentHitchMessage> getAllUnreadHitchMessage(String userId) {
		List<PersistentHitchMessage> messages = mapper.selectByParameters(MyBatisMapUtil.warp("user_id", userId));
		mapper.deleteByParameters(MyBatisMapUtil.warp("user_id", userId));
		return messages;
	}

}
