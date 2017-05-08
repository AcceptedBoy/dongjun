package com.gdut.dongjun.core.handler.thread;

import java.util.Date;

import com.gdut.dongjun.domain.vo.DeviceOnlineVO;
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
	private String id;
	private Integer type;
	
	public DeviceOnlineTask(String id, Integer type) {
		super(EXPIRED_TIME);
		this.id = id;
		this.type = type;
	}

	public DeviceOnlineTask(Integer executeTime, String id, Integer type) {
		super(executeTime);
		this.id = id;
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
			DeviceOnlineVO vo = new DeviceOnlineVO();
			vo.setId(id);
			vo.setStatus(0);
			vo.setDeviceType(type);
			vo.setDate(new Date());
			WebsiteServiceClient client = (WebsiteServiceClient)SpringApplicationContextHolder.getSpringBean("websiteServiceClient");
			client.getService().callbackDeviceOnline(vo);
		}
	}
	
	public void active() {
		this.active = true;
	}
	
}
