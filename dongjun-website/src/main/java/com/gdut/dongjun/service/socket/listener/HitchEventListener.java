package com.gdut.dongjun.service.socket.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.cxf.Hardware;
import com.gdut.dongjun.service.cxf.po.HighVoltageStatus;
import com.gdut.dongjun.service.cxf.po.SwitchGPRS;
import com.gdut.dongjun.util.CxfUtil;

@Component
public class HitchEventListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	public HitchEventListener(SimpMessagingTemplate template) {
		this.template = template;	
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if(event.getApplicationContext().getParent() == null) {
			Thread activeSwitchThread = new ActiveSwitchThread(template);
			activeSwitchThread.setDaemon(true);
			activeSwitchThread.start();
		}
	}
	
}

class ActiveSwitchThread extends Thread {
	
	@Autowired
	private HighVoltageHitchEventService eventService;
	
	private SimpMessagingTemplate template;
	
	public ActiveSwitchThread(SimpMessagingTemplate template) {
		this.template = template;	
	}
	
	@Override
	public synchronized void run() {
		
		while(true) {
			try {
				sleep(5000);
				this.template.convertAndSend("/topic/get_active_switch_status", 
						getActiveSwitchStatus());
				//this.template.convertAndSend("/topic/get_active_switch_status", 
				//		new Random().nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<ActiveHighSwitch> getActiveSwitchStatus() {
		Hardware client = CxfUtil.getHardwareClient(); 
		List<SwitchGPRS> switchs = client.getCtxInstance();
		List<ActiveHighSwitch> list = new ArrayList<>();
		//System.out.println(switchs.size());
		if(switchs != null) {
			for(SwitchGPRS s : switchs) {
				
				HighVoltageStatus status = client.getStatusbyId(s.getId());
				if(client.getStatusbyId(s.getId()) != null) {
					
					ActiveHighSwitch as = new ActiveHighSwitch();
					as.setId(s.getId());
					as.setOpen(s.isOpen());
					as.setStatus(status.getStatus());
					if(s.isOpen() == true) {
						HighVoltageHitchEvent event = eventService.getRecentHitchEvent(s.getId());
						if(event != null) {
							as.setHitchEventId(event.getId());
						}
					}
					list.add(as);
				}
			}
		}
		return list;
	}
}
