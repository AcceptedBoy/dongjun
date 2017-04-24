package com.gdut.dongjun.core.handler.thread;

/**
 * 设备有数据过来会建立这个任务，
 * 设备断开连接之后不会撤销此任务，
 * 而是等15分钟，中间没有任何报文过来了，就通知前端设备炸了，
 * 同时到CtxStore里删除掉这个任务的引用
 * @author Gordan_Deng
 * @date 2017年4月20日
 */
public class DeviceOnlineTask extends ScheduledTask {
	
	private static final int EXPIRED_TIME = 60 * 15;
	private boolean isActive = false;

	public DeviceOnlineTask(Integer executeTime) {
		super(executeTime);
	}

	@Override
	public void run() {
		if (isActive) {
			isActive = false;
			DeviceOnlineTask task = this;
			task.setExecuteTime(EXPIRED_TIME);
			ScheduledTaskExecutor.submit(task);
		} else {
			//TODO 通知前端设备下线了
			
		}
	}
	
}
