package com.gdut.dongjun.service.socket.listener;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.domain.po.HighVoltageHitchEvent;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.rmi.HardwareService;
import com.gdut.dongjun.service.rmi.po.HighVoltageStatus;
import com.gdut.dongjun.service.rmi.po.SwitchGPRS;

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
	
	/**
	 * rmi
	 */
	@Resource(name="hardwareService")
	private HardwareService hardwareService;
	
	@Override
	public synchronized void run() {
		
		while(true) {
			try {
				
				//this.template.convertAndSend("/topic/get_active_switch_status", 
				//		getActiveSwitchStatus());
				this.template.convertAndSend("/topic/get_active_switch_status", 
						getVisualData());
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
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
	
	 /**
     * @description TODO
     * @return
     */
    public Object getVisualData() {
    	
    	int i = new Random().nextInt(4);
    	if(i == 1) {
    		return generateActiveHighSwitchA();
    	} else if(i == 2) {
    		return generateActiveHighSwitchB();
    	} else {
    		return generateActiveHighSwitchC();
    	}
    }
    
    public Object generateActiveHighSwitchA() {
    	List<ActiveHighSwitch> list = new ArrayList<>();
    	ActiveHighSwitch as = new ActiveHighSwitch();
		as.setId("75ab61fafa814ce8a587eeb6a6693464");
		as.setOpen(false);
		as.setStatus("01");
		list.add(as);
		ActiveHighSwitch as2 = new ActiveHighSwitch();
		as2.setId("2bf5d3fec85c498c9f2c588e66c29ec9");
		as2.setOpen(true);
		as2.setStatus("00");
		list.add(as2);
		return list;
    }
    public Object generateActiveHighSwitchB() {
    	List<ActiveHighSwitch> list = new ArrayList<>();
    	ActiveHighSwitch as = new ActiveHighSwitch();
    	as.setId("193f7aacc04e452e9c8fa67841d69421");
    	as.setOpen(false);
    	as.setStatus("01");
    	list.add(as);
    	ActiveHighSwitch as2 = new ActiveHighSwitch();
    	as2.setId("1e6e4fb97ffa4f6caf5121e2c5e6e896");
    	as2.setOpen(false);
    	as2.setStatus("01");
    	list.add(as2);
    	return list;
    }
    
    public Object generateActiveHighSwitchC() {
    	List<ActiveHighSwitch> list = new ArrayList<>();
    	ActiveHighSwitch as = new ActiveHighSwitch();
    	as.setId("2bf5d3fec85c498c9f2c588e66c29ec9");
    	as.setOpen(false);
    	as.setStatus("00");
    	list.add(as);
    	ActiveHighSwitch as2 = new ActiveHighSwitch();
    	as2.setId("18edc1fd879a41119b2410ec66ce02ac");
    	as2.setOpen(false);
    	as2.setStatus("01");
    	list.add(as2);
    	ActiveHighSwitch as3 = new ActiveHighSwitch();
    	as3.setId("25a4d5f3752443c78e2dfe6189704e95");
    	as3.setOpen(false);
    	as3.setStatus("01");
    	list.add(as3);
    	return list;
    }
}
