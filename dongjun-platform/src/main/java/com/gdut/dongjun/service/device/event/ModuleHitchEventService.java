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
	
	/**
	 * 根据报警类型和报警设备id来搜索报警信息
	 * @param pre
	 * @param post
	 * @param moduleId
	 * @return
	 */
	List<ModuleHitchEvent> selectByTypeAndModuleId(int pre, int post, String moduleId);
}