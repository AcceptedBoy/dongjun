package com.gdut.dongjun.service;

import java.util.List;

import com.gdut.dongjun.domain.po.PersistentHitchMessage;
import com.gdut.dongjun.service.base.EnhancedService;

public interface PersistentHitchMessageService extends EnhancedService<PersistentHitchMessage> {

	/**
	 * 得到全部未读报警消息并删除未读记录，
	 * 需要注意的未读记录是每30天清除的 TODO
	 * @param userId
	 * @return
	 */
	public List<PersistentHitchMessage> getAllUnreadHitchMessage(String userId);
}
