package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.gdut.dongjun.domain.po.ControlMearsureCurrent;
import com.gdut.dongjun.domain.po.ControlMearsureVoltage;
import com.gdut.dongjun.domain.po.HighVoltageCurrent;
import com.gdut.dongjun.domain.po.HighVoltageVoltage;
import com.gdut.dongjun.domain.po.LowVoltageCurrent;
import com.gdut.dongjun.domain.po.LowVoltageVoltage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.ControlMearsureCurrentService;
import com.gdut.dongjun.service.ControlMearsureVoltageService;
import com.gdut.dongjun.service.HighVoltageCurrentService;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.HighVoltageVoltageService;
import com.gdut.dongjun.service.LowVoltageCurrentService;
import com.gdut.dongjun.service.LowVoltageVoltageService;
import com.gdut.dongjun.service.cxf.Hardware;
import com.gdut.dongjun.util.CxfUtil;

@Controller
@RequestMapping("/dongjun")
@SessionAttributes("currentUser")
public class CommandController {

	@Autowired
	private LowVoltageCurrentService currentService;
	@Autowired
	private LowVoltageVoltageService voltageService;
	@Autowired
	private HighVoltageCurrentService currentService2;
	@Autowired
	private HighVoltageVoltageService voltageService2;
	@Autowired
	private ControlMearsureCurrentService currentService3;
	@Autowired
	private ControlMearsureVoltageService voltageService3;
	@Autowired
	public HighVoltageHitchEventService eventService;
	
	private static final Logger logger = Logger
			.getLogger(CommandController.class);

	// @RequestMapping("/read_switch_status")
	// @ResponseBody
	// public String switchStatus(@RequestParam(required = true) String
	// switchId,
	// int sign, int type) {
	//
	//
	//
	// }

	/**
	 * 
	 * @Title: securityConfirm
	 * @Description: TODO
	 * @param @param controlCode
	 * @param @param session
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/security_confirm")
	@ResponseBody
	public Object securityConfirm(
			@RequestParam(required = true) String controlCode,
			HttpSession session) {

		User u = (User) session.getAttribute("currentUser");

		if (controlCode != null && u != null && u.getControlCode() != null
				&& controlCode.equals(u.getControlCode())) {
			
			return true;
		} else {

			return false;
		}
	}

	/**
	 * 
	 * @Title: controlSwitch
	 * @Description: TODO
	 * @param @param switchId
	 * @param @param sign
	 * @param @param orderTime
	 * @param @param model
	 * @param @param redirectAttributes
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/control_switch")
	public String controlSwitch(@RequestParam(required = true) String switchId,
			int sign, int type) {

		Hardware client = CxfUtil.getHardwareClient();
		String address = client.getOnlineAddressById(switchId);
		String msg = null;
		switch (sign) {
		case 0:// 开
			msg = client.generateOpenSwitchMessage(address, type);
			break;
		case 1:// 合
			msg = client.generateCloseSwitchMessage(address, type);
			break;
		default:
			break;
		}
		
		// 发送报文
		if (msg != null) {

			logger.info("发送开合闸报文" + msg);
		} else {
			return "error";
		}
		return "switch_detail";
	}

	/**
	 * 
	 * @Title: readVoltage
	 * @Description: TODO
	 * @param @param switchId
	 * @param @param sign
	 * @param @return
	 * @return Object
	 * @throws
	 */
	
	@RequestMapping("/read_voltage")
	@ResponseBody
	public void readVoltage(@RequestParam(required = true) final String switchId,
			final String type, HttpSession session) {
		
		final String userName;
		if(session.getAttribute("currentUser") != null) {
			userName = ((User) session.getAttribute("currentUser")).getName();
		} else {
			return;
		}
		Thread thread = new Thread(
			
			new Runnable() {
				public void run() {
					try {
						Thread.sleep(10000);
						template.convertAndSendToUser(userName, "/queue/read_voltage", 
								getVoltage(type, switchId));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		);
		thread.setDaemon(true);
		thread.start();
	}
	
	
	
	private Integer[] getVoltage(String type, String switchId) {
		Integer[] deStrings = new Integer[3];
		switch (type) {
		case "0":// 低压开关
			// 从数据库中查询结果
			List<LowVoltageVoltage> cliList = voltageService
					.getRecentlyVoltage();

			if (cliList != null) {
				for (LowVoltageVoltage current : cliList) {
					String p = current.getPhase();
					switch (p) {
					case "A":
						deStrings[0] = current.getValue();
						break;
					case "B":
						deStrings[1] = current.getValue();
						break;
					case "C":
						deStrings[2] = current.getValue();
						break;
					default:
						break;
					}
				}
			}
			break;
		case "1":// 高压开关
			// 从数据库中查询结果
			List<HighVoltageVoltage> cliList2 = voltageService2
					.getRecentlyVoltage(switchId, "A");
			if (cliList2 != null && cliList2.size() != 0) {
				deStrings[0] = cliList2.get(0).getValue();
			}
			
			cliList2 = voltageService2
					.getRecentlyVoltage(switchId, "B");
			if (cliList2 != null && cliList2.size() != 0) {
				deStrings[1] = cliList2.get(0).getValue();
			}
			cliList2 = voltageService2
					.getRecentlyVoltage(switchId, "C");
			if (cliList2 != null && cliList2.size() != 0) {
				deStrings[2] = cliList2.get(0).getValue();
			}
			break;
		case "2":// 管控开关
			// 从数据库中查询结果
			List<ControlMearsureVoltage> cliList3 = voltageService3
					.getRecentlyVoltage();

			if (cliList3 != null) {
				for (ControlMearsureVoltage current : cliList3) {
					String p = current.getPhase();
					switch (p) {
					case "A":
						deStrings[0] = current.getValue();
						break;
					case "B":
						deStrings[1] = current.getValue();
						break;
					case "C":
						deStrings[2] = current.getValue();
						break;
					default:
						break;
					}
				}

			}
			break;
		default:
			break;
		}
		return deStrings;
	}

	/**
	 * 
	 * @Title: readCurrentVariable
	 * @Description: TODO
	 * @param @param switchId
	 * @param @param sign
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/read_current")
	@ResponseBody
	public void readCurrentVariable(@RequestParam(required = true) 
		final String switchId, final String type, HttpSession session) {
		
		final String userName;
		if(session.getAttribute("currentUser") != null) {
			userName = ((User) session.getAttribute("currentUser")).getName();
		} else {
			return;
		}
		
		Thread thread = new Thread(
				
				new Runnable() {
					public void run() {
						try {
							Thread.sleep(10000);
							template.convertAndSendToUser(userName, "/queue/read_current", 
									getCurrnet(type, switchId));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			);
			thread.setDaemon(true);
			thread.start();
	}
	
	private Integer[] getCurrnet(String type, String switchId) {
		Integer[] deStrings = new Integer[3];
		switch (type) {
		case "0":// 低压开关
			// 从数据库中查询结果
			List<LowVoltageCurrent> cliList = currentService
					.getRecentlyCurrent();

			if (cliList != null) {
				for (LowVoltageCurrent current : cliList) {
					String p = current.getPhase();
					switch (p) {
					case "A":
						deStrings[0] = current.getValue();
						break;
					case "B":
						deStrings[1] = current.getValue();
						break;
					case "C":
						deStrings[2] = current.getValue();
						break;
					default:
						break;
					}
				}
			}
			break;
		case "1":// 高压开关
			// 从数据库中查询结果
			List<HighVoltageCurrent> cliList2 = currentService2
					.getRecentlyCurrent(switchId, "A");
			if(cliList2 != null && cliList2.size() != 0) {
				deStrings[0] = cliList2.get(0).getValue();
			}
			cliList2 = currentService2
					.getRecentlyCurrent(switchId, "B");
			if(cliList2 != null && cliList2.size() != 0) {
				deStrings[1] = cliList2.get(0).getValue();
			}
			cliList2 = currentService2
					.getRecentlyCurrent(switchId, "C");
			if(cliList2 != null && cliList2.size() != 0) {
				deStrings[2] = cliList2.get(0).getValue();
			}
			break;
		case "2":// 管控开关
			// 从数据库中查询结果
			List<ControlMearsureCurrent> cliList3 = currentService3
					.getRecentlyCurrent();

			if (cliList3 != null) {
				for (ControlMearsureCurrent current : cliList3) {
					String p = current.getPhase();
					switch (p) {
					case "A":
						deStrings[0] = current.getValue();
						break;
					case "B":
						deStrings[1] = current.getValue();
						break;
					case "C":
						deStrings[2] = current.getValue();
						break;
					default:
						break;
					}
				}

			}
			break;
		default:
			break;
		}
		return deStrings;
	}

	@RequestMapping("/read_lvswitch_status")
	@ResponseBody
	public Object read_lvswitch_status(String id) {

		//return CtxStore.get(id);
		return CxfUtil.getHardwareClient().getSwitchGPRS(id);
	}

	/**
	 * 
	 * @Title: read_hvswitch_status
	 * @Description: 高压开关状态
	 * @param @param id
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/read_hvswitch_status")
	@ResponseBody
	public void read_hvswitch_status(final String id, HttpSession session) {
		
		final String userName;
		if(session.getAttribute("currentUser") != null) {
			userName = ((User) session.getAttribute("currentUser")).getName();
		} else {
			return;
		}
		
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				template.convertAndSendToUser(userName, 
						"/queue/read_hv_status", 
						CxfUtil.getHardwareClient().getStatusbyId(id));
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
	
	@Autowired
	private SimpMessagingTemplate template;

    @Autowired
    public CommandController(SimpMessagingTemplate template) {
        this.template = template;
    }
	
	/*@RequestMapping("/hitch_event_spy")
	@ResponseBody
	public void getActiveSwitchStatus() {
		
		Thread thread = new ActiveSwitchThread(template);
		thread.setDaemon(true);
		thread.start();
		Hardware client = CxfUtil.getHardwareClient(); 
		List<SwitchGPRS> switchs = client.getCtxInstance();
		List<ActiveHighSwitch> list = new ArrayList<>();
		System.out.println(switchs.size());
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
		return list;
	}*/
	
	/*@RequestMapping("/get_active_switch_status")
	@ResponseBody
	public Object getActiveSwitchStatus() {
		
		Hardware client = CxfUtil.getHardwareClient(); 
		List<SwitchGPRS> switchs = client.getCtxInstance();
		List<ActiveHighSwitch> list = new ArrayList<>();
		System.out.println(switchs.size());
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
		return list;
	}*/
    
   
}

/*class ActiveSwitchThread extends Thread {
	
	@Autowired
	public HighVoltageHitchEventService eventService;
	
	private SimpMessagingTemplate template;

    @Autowired
    public ActiveSwitchThread(SimpMessagingTemplate template) {
        this.template = template;
    }
	
	@Override
	public synchronized void start() {
		
		
			try {
				sleep(5000);
				this.template.convertAndSend("/topic/get_active_switch_status", 
						getActiveSwitchStatus());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}
	
	public List<ActiveHighSwitch> getActiveSwitchStatus() {
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
}*/







