package com.gdut.dongjun.service.manager;

/**
 * 清除过期设备树的任务
 * @author Gordan_Deng
 * @date 2017年3月29日
 */
public class ExpiredSwitchTreeTask extends ScheduledTask {
	
	private String userId;
	
	public ExpiredSwitchTreeTask(String userId ,Integer executeTime) {
		super(executeTime);
		this.userId = userId;
	}

	@Override
	public void run() {
		UserHolder.removeUserSwitchTree(userId);
	}

}
