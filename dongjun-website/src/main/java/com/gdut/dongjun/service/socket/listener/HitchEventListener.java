package com.gdut.dongjun.service.socket.listener;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.rmi.HardwareService;
import com.gdut.dongjun.service.rmi.po.HighVoltageStatus;
import com.gdut.dongjun.service.rmi.po.SwitchGPRS;

@Component
public class HitchEventListener {
	
	@Autowired
	private HighVoltageHitchEventService eventService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	public HitchEventListener(SimpMessagingTemplate template) {
		this.template = template;	
	}
	
	@Resource(name="hardwareService")
	private HardwareService hardwareService;

	@Scheduled(initialDelay=3000, fixedDelay=10000)
	@Async
	public void activeSwitchStatusScheduled() throws ServletException, MessagingException, RemoteException {
		this.template.convertAndSend("/topic/get_active_switch_status", 
				getActiveSwitchStatus());
	}
	
	private List<ActiveHighSwitch> getActiveSwitchStatus() throws RemoteException {
		
		List<SwitchGPRS> switchs = hardwareService.getCtxInstance();
		List<ActiveHighSwitch> list = new ArrayList<>();
		//System.out.println(switchs.size());
		if(switchs != null) {
			for(SwitchGPRS s : switchs) {
				
				HighVoltageStatus status = hardwareService.getStatusbyId(s.getId());
				if(hardwareService.getStatusbyId(s.getId()) != null) {
					
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