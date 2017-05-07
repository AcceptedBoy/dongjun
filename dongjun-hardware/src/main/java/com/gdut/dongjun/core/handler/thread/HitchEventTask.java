package com.gdut.dongjun.core.handler.thread;

import com.gdut.dongjun.domain.po.ElectronicModuleHitchEvent;
import com.gdut.dongjun.domain.po.abstractmodel.AbstractHitchEvent;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.service.ElectronicModuleHitchEventService;
import com.gdut.dongjun.service.base.BaseService;
import com.gdut.dongjun.service.base.EnhancedService;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;
import com.gdut.dongjun.util.SpringApplicationContextHolder;

/**
 * 实际上执行细节在这个类
 * @author Gordan_Deng
 * @date 2017年3月8日
 */
public abstract class HitchEventTask<T extends AbstractHitchEvent> implements Runnable {
	
	protected T hitchEvent;
	
	protected EnhancedService<T> service;
	
	protected static WebsiteServiceClient websiteServiceClient;
	
	public HitchEventTask(EnhancedService<T> service, T hitchEvent) {
		super();
		this.hitchEvent = hitchEvent;
		this.service = service;
	}

	protected HitchEventTask() {
		if (null == websiteServiceClient) {
			synchronized(HitchEventTask.class) {
				if (null == websiteServiceClient) {
					HitchEventTask.websiteServiceClient = (WebsiteServiceClient)SpringApplicationContextHolder.getSpringBean("websiteServiceClient");
				}
			}
		}
	}

	@Override
	public void run() {
		doPreHandle();
		
		sendHitchEvent(hitchEvent);
		
		doPostHandle();
	}
	
	/**
	 * 进行发送报警消息前的操作
	 */
	public abstract void doPreHandle();
	
	/**
	 * 进行发送报警消息后的操作
	 */
	public abstract void doPostHandle();
	
	/**
	 * 发送报警消息
	 * @param event
	 */
	public void sendHitchEvent(T event) {
		websiteServiceClient.getService().callbackHitchEvent(wrapHitchEventVO(event));
	}
	
	/**
	 * 包装成VO
	 * @param event
	 * @return
	 */
	public HitchEventVO wrapHitchEventVO(AbstractHitchEvent event) {
		HitchEventVO vo = new HitchEventVO();
		vo.setId(event.getId());
		vo.setMonitorId(event.getMonitorId());
		vo.setGroupId(event.getGroupId());
		vo.setType(event.getType());
		return vo;
	}

	protected EnhancedService<T> getService() {
		return service;
	}

	protected void setService(EnhancedService<T> service) {
		this.service = service;
	}

}
