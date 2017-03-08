package com.gdut.dongjun.core.handler.thread;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdut.dongjun.domain.po.abstractmodel.AbstractHitchEvent;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.service.base.BaseService;
import com.gdut.dongjun.service.webservice.client.WebsiteServiceClient;

/**
 * 实际上执行细节在这个类
 * @author Gordan_Deng
 * @date 2017年3月8日
 */
public abstract class HitchEventThread implements Runnable {
	
	protected AbstractHitchEvent hitchEvent;
	
	protected BaseService service;
	
	@Autowired
	protected static WebsiteServiceClient websiteServiceClient;

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
	public void sendHitchEvent(AbstractHitchEvent event) {
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
		vo.setGroupId(event.getGroupId());
		vo.setHitchReason(vo.getHitchReason());
		vo.setHitchTime(event.getHitchTime());
		vo.setSwitchId(event.getSwitchId());
		vo.setType(event.getType());
		return vo;
	}

	protected BaseService getService() {
		return service;
	}

	protected void setService(BaseService service) {
		this.service = service;
	}
}
