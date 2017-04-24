package com.gdut.dongjun.core.handler.thread;

import com.gdut.dongjun.core.GPRSCtxStore;

/**
 * 如果GPRS超时就删除<code>GPRSCtxStore.GPRSList</code>中的值
 * 15分钟内如果GPRS有登录包、心跳包来，就设置<code>isConnected</code>为true，刷新任务状态
 * @author Gordan_Deng
 * @date 2017年4月15日
 */
public class GPRSExpiredTask extends ScheduledTask {
	
	private static final int EXPIRED_TIME = 60 * 15;
	private boolean isConnected = false;
	private String address;
	
	public void GPRSConnected() {
		isConnected = true;
	}

	public GPRSExpiredTask(String address) {
		super(EXPIRED_TIME);
		this.address = address;
	}

	@Override
	public void run() {
		if (isConnected) {
			isConnected = false;
			GPRSExpiredTask task = this;
			task.setExecuteTime(EXPIRED_TIME);
			ScheduledTaskExecutor.submit(task);
		} else {
			GPRSCtxStore.removeGPRS(address);
		}
	}
	
	public String getAddress() {
		return this.address;
	}

}
