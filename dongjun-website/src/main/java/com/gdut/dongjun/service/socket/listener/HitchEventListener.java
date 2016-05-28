package com.gdut.dongjun.service.socket.listener;

import java.rmi.RemoteException;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.rmi.HardwareService;

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

	@Scheduled(initialDelay=3000, fixedRate=15000)
	@Async
	public void activeSwitchStatusScheduled() throws ServletException, MessagingException, RemoteException {
		if(hardwareService.whetherChangeInfo()) {
			this.template.convertAndSend("/topic/get_active_switch_status", 
					hardwareService.getActiveSwitchStatus());
		}
		/*this.template.convertAndSend("/topic/get_active_switch_status", 
				getVisualData());*/
	}
	
	//*************
	public ActiveHighSwitch getVisualData() {
		
		ActiveHighSwitch status = new ActiveHighSwitch();
		status.setId("75ab61fafa814ce8a587eeb6a6693464");
		status.setOpen(false);
		status.setOpen(false);
		return status;
	}
}