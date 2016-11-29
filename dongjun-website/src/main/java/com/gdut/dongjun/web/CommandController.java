package com.gdut.dongjun.web;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.gdut.dongjun.domain.HighVoltageStatus;
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
import com.gdut.dongjun.service.HighSwitchUserService;
import com.gdut.dongjun.service.HighVoltageCurrentService;
import com.gdut.dongjun.service.HighVoltageHitchEventService;
import com.gdut.dongjun.service.HighVoltageVoltageService;
import com.gdut.dongjun.service.LowVoltageCurrentService;
import com.gdut.dongjun.service.LowVoltageVoltageService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.rmi.HardwareService;
import com.gdut.dongjun.thread.manager.DefaultThreadManager;
import com.gdut.dongjun.thread.manager.MsgPushThreadManager;

@Controller
@RequestMapping("/dongjun")
@SessionAttributes("currentUser")
public class CommandController {
	
	static List<ActiveHighSwitch> delSwitch = new ArrayList<ActiveHighSwitch>();
	
	static List<ActiveHighSwitch> switches = new ArrayList<ActiveHighSwitch>();
	
	static boolean isExcute = false;

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
	private HighVoltageHitchEventService eventService;
	@Autowired
	private HighSwitchUserService highOperatorService;
	@Autowired
	private UserService userService;
	
	@Resource(name="hardwareService")
	private HardwareService hardwareService;
	
	private final SimpMessagingTemplate template;

	@Autowired
	public CommandController(SimpMessagingTemplate messagingTemplate) {
		this.template = messagingTemplate;
	}
	
	private static final Logger logger = Logger
			.getLogger(CommandController.class);

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
	 * TODO
	 * 因为开关的渲染是要等到开关信息有变化的时候才会发送过来，
	 * 所以当用户刷新页面的时候不能及时地获取到开关的在线信息
	 * <p>这里的接口实现就是可以忽略开关变化获取所以开关在线信息
	 * <p>需要注意的地方就是，因为这个是前端{@code index.js}文件一刚开始刷新的时候就发送这个请求，那个
	 * 时候如果websocket还没完成订阅而发送信息的话，浏览器可能无法接收到信息；所以，发送信息之前要
	 * 延时几秒，等页面加载完，订阅完，再通过{@code template}发送在线开关信息
	 */
	@RequestMapping("/get_active_switch_ignore_change")
	@ResponseBody
	public void getActiveSwitchIgnoreChange(HttpSession session) throws MessagingException, RemoteException {
		final User _user = (User)session.getAttribute("currentUser");
		DefaultThreadManager.delayExecute(new Runnable() {
			@Override
			public void run() {
				try {
					if (!isExcute) {
						getActive();
						delActive(_user);
						addActive(_user);
						changeActive(_user);
						isExcute = true;
					}
					template.convertAndSend("/topic/get_active_switch_status", 
//							hardwareService.getActiveSwitchStatus());
							switches);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}, 8);
	}
	
	public void delActive(User user) {
		MsgPushThreadManager.createScheduledPoolDaemonThread(
				new Runnable() {

					@Override
					public void run() {
						DecimalFormat format = new DecimalFormat("0");
						int index = Integer.parseInt(format.format(Math.random()*2));
						delSwitch.add(switches.get(index));
						switches.remove(index);
					}
					
				}, 8, user.getId());
	}
	
	public void changeActive(User user) {
		
		MsgPushThreadManager.createScheduledPoolDaemonThread(
				new Runnable() {

					@Override
					public void run() {
						DecimalFormat format = new DecimalFormat("0");
						int index = Integer.parseInt(format.format(Math.random()*10));
						ActiveHighSwitch active = switches.get(2);
						if (active.getStatus().equals("00")) {
							active.setOpen(true);
							active.setStatus("01");
						}
						else {
							active.setStatus("00");
							active.setOpen(false);
						}
					}
					
				}, 10, user.getId());
	}
	
	public void addActive(User user) {
		MsgPushThreadManager.createScheduledPoolDaemonThread(
				new Runnable() {

					@Override
					public void run() {
						if (!delSwitch.isEmpty()) {
							switches.add(delSwitch.get(0));
							delSwitch.remove(0);
						}
					}
					
				}, 15, user.getId());
	}
	
	public void getActive() {
		List<ActiveHighSwitch> list = new ArrayList<ActiveHighSwitch>();
//		ActiveHighSwitch active = new ActiveHighSwitch();
//		active.setOpen(true);
//		active.setStatus("01");
//		active.setHitchEventId(null);
//		active.setId("03");
//		list.add(active);
//		ActiveHighSwitch active1 = new ActiveHighSwitch();
//		active1.setOpen(true);
//		active1.setStatus("01");
//		active1.setHitchEventId(null);
//		active1.setId("028d7316cbd94221857264dcd0be643f");
//		list.add(active1);
//		ActiveHighSwitch active2 = new ActiveHighSwitch();
//		active2.setOpen(false);
//		active2.setStatus("00");
//		active2.setHitchEventId(null);
//		active2.setId("25a4d5f3752443c78e2dfe6189704e95");
//		list.add(active2);
//		ActiveHighSwitch active3 = new ActiveHighSwitch();
//		active3.setOpen(true);
//		active3.setStatus("01");
//		active3.setHitchEventId(null);
//		active3.setId("cb2f0e23c0284064ac0faba9f7dc303a");
//		list.add(active3);
		for(int i=0;i<100;i++){
			//HighVoltageSwitch a1=SwitchService.selectByPrimaryKey(i+"");
			if(i%2==0){
				ActiveHighSwitch active3 = new ActiveHighSwitch();
				active3.setOpen(true);
				active3.setStatus("01");
				active3.setHitchEventId(null);
				active3.setId(i+"");
				list.add(active3);
			}else{
				ActiveHighSwitch active3 = new ActiveHighSwitch();
				active3.setOpen(false);
				active3.setStatus("00");
				active3.setHitchEventId(null);
				active3.setId(i+"");
				list.add(active3);
			}
		}
//		ActiveHighSwitch active2 = new ActiveHighSwitch();
//		active2.setOpen(true);
//		active2.setStatus("00");
//		active2.setHitchEventId(null);
//		active2.setId("25a4d5f3752443c78e2dfe6189704e95");
//		list.add(active2);
		this.switches.addAll(list);
	}
	

	@RequestMapping("/control_switch")
	@ResponseBody
	public String controlSwitch(@RequestParam(required = true) String switchId,
			int sign, int type) throws RemoteException {
		
		String address = hardwareService.getOnlineAddressById(switchId);
		String msg = null;
		
		switch (sign) {
		case 0:// 开
			msg = hardwareService.generateOpenSwitchMessage(address, type);
			break;
		case 1:// 合
			msg = hardwareService.generateCloseSwitchMessage(address, type);
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
		return "success";
	}
	
	@RequestMapping("/read_voltage")
	@ResponseBody
	public void readVoltage(@RequestParam(required = true) final String switchId,
			final String type, HttpSession session) {
		
		User user = null;
		if(session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		} else {
			return;
		}
		/*MsgPushThreadManager.createScheduledPoolDaemonThread(
				getVoltageRunnable(user.getName(), type, switchId), 6, user.getId());*/
		MsgPushThreadManager.createScheduledPoolDaemonThread(
				getVoltageRunnable(user.getName(), type, switchId), 6, user.getId());
	}
	
	@RequestMapping("/stop_read_param")
	@ResponseBody
	public void stopReadVoltage(HttpSession session) {
		
		User user = null;
		if(session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		} else {
			return;
		}
		MsgPushThreadManager.finishScheduledByUser(user.getId());
	}

	/**
	 * TODO 
	 */
	private Runnable getVoltageRunnable(final String userName, 
			final String type, final String switchId) {
		return new Runnable() {
			public void run() {
//				template.convertAndSendToUser(userName, "/queue/read_voltage", 
//						getVoltage(type, switchId));
				template.convertAndSendToUser(userName, "/queue/read_voltage", 
						getVoltVisual());
			}
		};
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
	 * 通过前端发送请求，获取开关的id，从而读取开关电压之，再根据socket的订阅，定点推送到特定的人
	 */
	@RequestMapping("/read_current")
	@ResponseBody
	public void readCurrentVariable(@RequestParam(required = true) 
		final String switchId, final String type, HttpSession session) {
		
		User user = null;
		if(session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		}
		MsgPushThreadManager.createScheduledPoolDaemonThread(
				getCurrentRunnable(user.getName(), type, switchId), 6, user.getId());
	}
	
	/**
	 * TODO
	 */
	private Runnable getCurrentRunnable(final String userName,
			final String type, final String switchId) {
		return new Runnable() {
			public void run() {
//				template.convertAndSendToUser(userName, "/queue/read_current", 
//						getCurrent(type, switchId));
				template.convertAndSendToUser(userName, "/queue/read_current", 
						getCurrVisual());
			}
		};
	}
	
	private Integer[] getCurrent(String type, String switchId) {
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
	public Object read_lvswitch_status(String id) throws RemoteException {

		return hardwareService.getSwitchGPRS(id);
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
		
		final User user;
		if(session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		} else {
			return;
		}
		
		MsgPushThreadManager.createScheduledPoolDaemonThread(
				new Runnable() {

					@Override
					public void run() {
						try {
							
							//TODO
//							HighVoltageStatus status = hardwareService.getStatusbyId(id);
//							if(status != null) {
//								template.convertAndSendToUser(user.getName(), 
//										"/queue/read_hv_status", status);
//							}
							template.convertAndSendToUser(user.getName(), 
									"/queue/read_hv_status", getStatusVisual());
						} catch (MessagingException e1) {
							e1.printStackTrace();
						}
					}
				}, 6, user.getId());
		
	}
	
	//**************************************
	// TODO 虚拟数据获取
	//**************************************
	
	public HighVoltageStatus getStatusVisual() {
		
		int i = new Random().nextInt(4);
		if(i == 1) {
			HighVoltageStatus status = new HighVoltageStatus();
			status.setChong_he_zha("01");
			status.setGuo_liu_er_duan("00");
			status.setGuo_liu_yi_duan("00");
			status.setLing_xu_guo_liu_("01");
			status.setGuo_liu_san_duan("00");
			status.setStatus("00");
			status.setPt1_guo_ya("01");
			status.setPt1_you_ya("00");
			status.setPt2_guo_ya("00");
			status.setPt2_you_ya("01");
			status.setJiao_liu_shi_dian("10");
			status.setLing_xu_guo_liu_("20");
			status.setShou_dong_fen_zha("01");
			status.setShou_dong_he_zha("01");
			status.setYao_kong_fen_zha("01");
			status.setYao_kong_he_zha("01");
			status.setYao_kong_fu_gui("01");
			return status;
		} else if(i == 2){
			HighVoltageStatus status = new HighVoltageStatus();
			status.setChong_he_zha("01");
			status.setGuo_liu_er_duan("00");
			status.setGuo_liu_yi_duan("01");
			status.setLing_xu_guo_liu_("00");
			status.setGuo_liu_san_duan("00");
			status.setStatus("01");
			status.setPt1_guo_ya("01");
			status.setPt1_you_ya("00");
			status.setPt2_guo_ya("00");
			status.setPt2_you_ya("01");
			status.setJiao_liu_shi_dian("10");
			status.setLing_xu_guo_liu_("20");
			status.setShou_dong_fen_zha("01");
			status.setShou_dong_he_zha("01");
			status.setYao_kong_fen_zha("01");
			status.setYao_kong_he_zha("01");
			status.setYao_kong_fu_gui("01");
			return status;
		} else {
			HighVoltageStatus status = new HighVoltageStatus();
			status.setChong_he_zha("01");
			status.setGuo_liu_er_duan("00");
			status.setGuo_liu_yi_duan("01");
			status.setLing_xu_guo_liu_("00");
			status.setGuo_liu_san_duan("01");
			status.setStatus("00");
			status.setPt1_guo_ya("01");
			status.setPt1_you_ya("00");
			status.setPt2_guo_ya("00");
			status.setPt2_you_ya("01");
			status.setJiao_liu_shi_dian("10");
			status.setLing_xu_guo_liu_("20");
			status.setShou_dong_fen_zha("01");
			status.setShou_dong_he_zha("01");
			status.setYao_kong_fen_zha("01");
			status.setYao_kong_he_zha("01");
			status.setYao_kong_fu_gui("01");
			return status;
		}
	}
	
	
    
	private Integer[] getCurrVisual() {
    	
    	int i = new Random().nextInt(4);
    	if(i == 1) {
    		return new Integer[] {345, 0,0};
    	} else if(i == 2){
    		return new Integer[] {166, 0, 0};
    	} else {
    		return new Integer[] {452, 0, 0};
    	}
    }
	
	private Integer[] getVoltVisual() {
    	
    	int i = new Random().nextInt(4);
    	if(i == 1) {
    		return new Integer[] {12345, 0,0};
    	} else if(i == 3) {
    		return new Integer[] {11266, 0, 0};
    	} else {
    		return new Integer[] {16726, 0, 0};
    	}
    }

}





