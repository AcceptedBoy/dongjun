package com.gdut.dongjun.service.device.event;

import java.util.List;

import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.service.base.EnhancedService;

public interface ModuleHitchEventService extends EnhancedService<ModuleHitchEvent> {

	/**
	 * pre是>=
	 * post是<
	 * 
	 * 例如：
	 * 		pre = 301 post 302
	 * 		sql语句就是 type >=301 and type < 302
	 * @param pre
	 * @param post
	 * @param groupId
	 * @return
	 */
	List<ModuleHitchEvent> selectByType(int pre, int post, String groupId);
}