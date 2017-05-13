package com.gdut.dongjun.core.handler.thread;

import java.util.Date;

import com.gdut.dongjun.core.CtxStore;
import com.gdut.dongjun.core.ElectronicCtxStore;
import com.gdut.dongjun.core.TemperatureCtxStore;
import com.gdut.dongjun.core.handler.ChannelInfo;
import com.gdut.dongjun.domain.dto.DeviceOnlineDTO;
import com.gdut.dongjun.domain.dto.InfoEventDTO;
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
	private String monitorId;
	private String moduleId;
	private String groupId;
	private Integer type;
	
	public DeviceOnlineTask(String moduleId, Integer type) {
		super(EXPIRED_TIME);
		this.moduleId = moduleId;
		this.type = type;
	}

	public DeviceOnlineTask(Integer executeTime, String moduleId, Integer type) {
		super(executeTime);
		this.moduleId = moduleId;
		this.type = type;
	}

	@Override
	public void run() {
		if (active) {
			active = false;
			DeviceOnlineTask task = this;	
			task.setExecuteTime(EXPIRED_TIME);
			ScheduledTaskExecutor.submit(task);
		} else {
			//TODO 通知前端设备下线了
			InfoEventDTO  dto = new InfoEventDTO();
			dto.setGroupId(groupId);
			dto.setMonitorId(monitorId);
			dto.setModuleId(moduleId);
			dto.setType(type);
			dto.setId(null);
			WebsiteServiceClient client = (WebsiteServiceClient)SpringApplicationContextHolder.getSpringBean("websiteServiceClient");
			client.getService().callbackInfoEvent(dto);
		}
	}
	
	public void active() {
		this.active = true;
	}
	
}
