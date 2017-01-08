package com.gdut.dongjun.service.socket.listener;

import com.gdut.dongjun.service.device.event.HighVoltageHitchEventService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.rmi.RemoteException;

/**
 * 监听器，当有开关变化推送至页面
 * @author AcceptedBoy
 */
@Component
public class HitchEventListener {
	
	@Autowired
	private HighVoltageHitchEventService eventService;
	
	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	public HitchEventListener(SimpMessagingTemplate messagingTemplate) {
		this.template = messagingTemplate;
	}
	
	@Autowired
	private HardwareServiceClient hardwareClient;

	/**
	 * 每15秒就询问硬件系统，若硬件信息有变化，则将硬件信息推送出去
	 */
	@Scheduled(initialDelay=3000, fixedRate=15000)
	@Async
	public void activeSwitchStatusScheduled() throws ServletException, MessagingException, RemoteException {
		/*if(hardwareClient.getService().whetherChangeInfo()) {
			this.template.convertAndSend("/topic/get_active_switch_status",
					hardwareClient.getService().getActiveSwitchStatus());
		}*/
		/*this.template.convertAndSend("/topic/get_active_switch_status", 
				getVisualData());*/
	}
	
	//*************
	/*public List<ActiveHighSwitch> getVisualData() {
		int num = new Random().nextInt(3);
		if(num == 1) {
			return getVisualDataA();
		} else {
			return getVisualDataB();
		}
	}
	
	public List<ActiveHighSwitch> getVisualDataA() {
		
		List<ActiveHighSwitch> status = new ArrayList<ActiveHighSwitch>();
		status.add(new ActiveHighSwitch());
		status.add(new ActiveHighSwitch("001ca44cf523491186106cdee1533827", false, "00", null));
		status.add(new ActiveHighSwitch("44578db3e4a740b9a4b625c178dee85f", false, "00", null));
		status.add(new ActiveHighSwitch("4f5cc07e8aa945d38446949031bcee65", false, "00", null));
		status.add(new ActiveHighSwitch("6c7aed980770436d8da4f49c8760c1db", true, "00", null));
		status.add(new ActiveHighSwitch("75ab61fafa814ce8a587eeb6a6693464", false, "01", null));
		status.add(new ActiveHighSwitch("7739639ee6ed4a64aa4004a7e8d896a8", false, "01", null));
		status.add(new ActiveHighSwitch("81f03ab5cafc468f98e033bfd5724735", false, "01", null));
		status.add(new ActiveHighSwitch("89a82bd7146e4bf8baac07f00fe13bcd", false, "01", null));
		return status;
	}
	
	public List<ActiveHighSwitch> getVisualDataB() {
		
		List<ActiveHighSwitch> status = new ArrayList<ActiveHighSwitch>();
		status.add(new ActiveHighSwitch());
		status.add(new ActiveHighSwitch("a60ac97fb7cc4912bf11a8fca5042cb0", false, "00", null));
		status.add(new ActiveHighSwitch("44578db3e4a740b9a4b625c178dee85f", false, "00", null));
		status.add(new ActiveHighSwitch("4f5cc07e8aa945d38446949031bcee65", false, "00", null));
		status.add(new ActiveHighSwitch("6c7aed980770436d8da4f49c8760c1db", false, "01", null));
		status.add(new ActiveHighSwitch("75ab61fafa814ce8a587eeb6a6693464", false, "00", null));
		status.add(new ActiveHighSwitch("7601a1295ccb44ca8337e8ea31b96919", false, "01", null));
		status.add(new ActiveHighSwitch("81f03ab5cafc468f98e033bfd5724735", false, "01", null));
		status.add(new ActiveHighSwitch("89a82bd7146e4bf8baac07f00fe13bcd", false, "01", null));
		return status;
	}*/
}