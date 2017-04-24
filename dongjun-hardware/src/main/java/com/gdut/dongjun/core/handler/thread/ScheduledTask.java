package com.gdut.dongjun.core.handler.thread;

/**
 * 延迟执行的任务
 * @author Gordan_Deng
 * @date 2017年3月29日
 */
public abstract class ScheduledTask implements Runnable {

	//在循环队列中的位置
	protected Integer serialNumber;
	
	//还有多少圈循环后才被执行
	protected Integer round;
	
	//原本设定在多少秒后执行，但是是秒
	protected Integer executeTime;
	
	protected boolean isAvailable;

	public ScheduledTask(Integer executeTime) {
		super();
		this.round = executeTime / ScheduledTaskExecutor.ROUND_TIME;
		this.executeTime = executeTime;
		this.isAvailable = true;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Integer getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Integer executeTime) {
		this.executeTime = executeTime;
		this.round = executeTime / ScheduledTaskExecutor.ROUND_TIME;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public void cancel() {
		this.isAvailable = false;
	}
	
}
