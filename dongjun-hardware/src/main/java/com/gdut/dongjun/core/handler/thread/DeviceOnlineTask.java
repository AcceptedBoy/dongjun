package com.gdut.dongjun.core.handler.thread;

import com.gdut.dongjun.domain.dto.InfoEventDTO;
import com.gdut.dongjun.domain.po.ModuleInfoEvent;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;
import com.gdut.dongjun.util.SpringApplicationContextHolder;

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
	private boolean active = false;
	private ModuleInfoEvent infoEvent;
	
	public DeviceOnlineTask(ModuleInfoEvent infoEvent) {
		super(EXPIRED_TIME);
		this.infoEvent = infoEvent;
	}

	public DeviceOnlineTask(Integer executeTime, ModuleInfoEvent infoEvent) {
		super(executeTime);
		this.infoEvent = infoEvent;
	}

	@Override
	public void run() {
		if (active) {
			active = false;
			DeviceOnlineTask task = this;	
			task.setExecuteTime(EXPIRED_TIME);
			ScheduledTaskExecutor.submit(task);
		} else {
			//通知前端设备下线
			InfoEventDTO  dto = new InfoEventDTO();
			dto.setGroupId(infoEvent.getGroupId());
			dto.setModuleId(infoEvent.getModuleId());
			dto.setMonitorId(infoEvent.getMonitorId());
			dto.setId(infoEvent.getId());
			dto.setType(infoEvent.getType());
			dto.setText(new Integer(0));
			WebsiteServiceClient client = (WebsiteServiceClient)SpringApplicationContextHolder.getSpringBean("websiteServiceClient");
			client.getService().callbackInfoEvent(dto);
		}
	}
	
	public void active() {
		this.active = true;
	}
	
}
