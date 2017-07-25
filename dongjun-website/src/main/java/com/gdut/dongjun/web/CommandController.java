package com.gdut.dongjun.web;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.OperationLogService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.common.DeviceBinding;
import com.gdut.dongjun.service.device.DeviceCommonService;
import com.gdut.dongjun.service.device.current.HighVoltageCurrentService;
import com.gdut.dongjun.service.thread.manager.DefaultThreadManager;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;

@Controller
@RequestMapping("/dongjun")
@SessionAttributes("currentUser")
public class CommandController {

	static List<ActiveHighSwitch> delSwitch = new ArrayList<ActiveHighSwitch>();

	static List<ActiveHighSwitch> switches = new ArrayList<ActiveHighSwitch>();

	static boolean isExcute = false;

	@Autowired
	private DeviceCommonService deviceCommonService;
	@Autowired
	private HardwareServiceClient hardwareClient;
	@Autowired
	private UserService userService;
	@Autowired
	private OperationLogService oLogService;
	@Autowired
	private HighVoltageCurrentService currentService;
	
	private final SimpMessagingTemplate template;

	@Autowired
	public CommandController(SimpMessagingTemplate messagingTemplate) {
		this.template = messagingTemplate;
	}

	private static final Logger logger = Logger.getLogger(CommandController.class);

	@RequestMapping("/security_confirm")
	@ResponseBody
	public Object securityConfirm(@RequestParam(required = true) String controlCode, HttpSession session) {

		User u = (User) session.getAttribute("currentUser");

		if (controlCode != null && u != null && u.getControlCode() != null && controlCode.equals(u.getControlCode())) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 未来改进接口，需要做安全措施，防止脚本刷接口 能不能通过注解来做呢？未来的防刷接口都通过这种注解来做。然后如果用户在一段时间里刷
	 * 一定数量就禁止访问，一段时间内刷超多数量直接禁止用户一段时间访问网站，这个可以通过filter来做。
	 */
	// @RequestMapping("/security_confirm")
	// @ResponseBody
	// public ResponseMessage securityConfirm(
	// @RequestParam(required = true) String controlCode,
	// @RequestParam(required = true) String switchId,
	// @RequestParam(required = true) int sign,
	// @RequestParam(required = true) int type,
	// HttpSession session) {
	//
	// User u = (User) session.getAttribute("currentUser");
	//
	// if (controlCode != null && u != null && u.getControlCode() != null
	// && controlCode.equals(u.getControlCode())) {
	// return controlSwitch(switchId, sign, type, u);
	// } else {
	// return ResponseMessage.warning("操作码错误");
	// }
	// }

	/**
	 * TODO 因为开关的渲染是要等到开关信息有变化的时候才会发送过来， 所以当用户刷新页面的时候不能及时地获取到开关的在线信息
	 * <p>
	 * 这里的接口实现就是可以忽略开关变化获取所以开关在线信息
	 * <p>
	 * 需要注意的地方就是，因为这个是前端{@code index.js}文件一刚开始刷新的时候就发送这个请求，那个
	 * 时候如果websocket还没完成订阅而发送信息的话，浏览器可能无法接收到信息；所以，发送信息之前要 延时几秒，等页面加载完，订阅完，再通过
	 * {@code template}发送在线开关信息 关于设备状态如何描述的问题，open属性是用来描述是不是跳闸，status用来描述设备的状态。
	 * status 00分闸，01合闸，status为空代表没上线；open为true代表设备跳闸，false代表设备正常运行。
	 * 特别地，当open为true，status为00时，代表用户执行了分闸，会报警。
	 */
	@RequestMapping("/get_active_switch_ignore_change")
	@ResponseBody
	public void getActiveSwitchIgnoreChange(HttpSession session) throws MessagingException, RemoteException {
		final User _user = (User) session.getAttribute("currentUser");

		DefaultThreadManager.delayExecute(new Runnable() {
			@Override
			public void run() {
				try {
					/* 测试用代码 */
					// if (!isExcute) {
					// getActive();
					// delActive(_user);
					// addActive(_user);
					// changeActive(_user);
					// isExcute = true;
					// }
					template.convertAndSend("/topic/get_active_switch_status",
							// hardwareService.getActiveSwitchStatus());
							hardwareClient.getService().getActiveSwitchStatus());
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}, 8);
	}

	@RequestMapping("/get_active_switch")
	@ResponseBody
	public Object getActiveSwitch(HttpSession session) throws MessagingException, RemoteException {
		final User _user = (User) session.getAttribute("currentUser");
		return hardwareClient.getService().getActiveSwitchStatus();
	}

	// public void delActive(User user) {
	// MsgPushThreadManager.createScheduledPoolDaemonThread(
	// new Runnable() {
	//
	// @Override
	// public void run() {
	// DecimalFormat format = new DecimalFormat("0");
	// int index = Integer.parseInt(format.format(Math.random()*2));
	// delSwitch.add(switches.get(index));
	// switches.remove(index);
	// }
	//
	// }, 8, user.getId());
	// }

	// public void changeActive(User user) {
	//
	// MsgPushThreadManager.createScheduledPoolDaemonThread(
	// new Runnable() {
	//
	// @Override
	// public void run() {
	// DecimalFormat format = new DecimalFormat("0");
	// int index = Integer.parseInt(format.format(Math.random()*10));
	// ActiveHighSwitch active = switches.get(2);
	// if (active.getStatus().equals("00")) {
	// active.setOpen(true);
	// active.setStatus("01");
	// }
	// else {
	// active.setStatus("00");
	// active.setOpen(false);
	// }
	// }
	//
	// }, 10, user.getId());
	// }

	// public void addActive(User user) {
	// MsgPushThreadManager.createScheduledPoolDaemonThread(
	// new Runnable() {
	//
	// @Override
	// public void run() {
	// if (!delSwitch.isEmpty()) {
	// switches.add(delSwitch.get(0));
	// delSwitch.remove(0);
	// }
	// }
	//
	// }, 15, user.getId());
	// }

	// public void getActive() {
	// List<ActiveHighSwitch> list = new ArrayList<ActiveHighSwitch>();
	//// ActiveHighSwitch active = new ActiveHighSwitch();
	//// active.setOpen(true);
	//// active.setStatus("01");
	//// active.setHitchEventId(null);
	//// active.setId("03");
	//// list.add(active);
	//// ActiveHighSwitch active1 = new ActiveHighSwitch();
	//// active1.setOpen(true);
	//// active1.setStatus("01");
	//// active1.setHitchEventId(null);
	//// active1.setId("028d7316cbd94221857264dcd0be643f");
	//// list.add(active1);
	//// ActiveHighSwitch active2 = new ActiveHighSwitch();
	//// active2.setOpen(false);
	//// active2.setStatus("00");
	//// active2.setHitchEventId(null);
	//// active2.setId("25a4d5f3752443c78e2dfe6189704e95");
	//// list.add(active2);
	//// ActiveHighSwitch active3 = new ActiveHighSwitch();
	//// active3.setOpen(true);
	//// active3.setStatus("01");
	//// active3.setHitchEventId(null);
	//// active3.setId("cb2f0e23c0284064ac0faba9f7dc303a");
	//// list.add(active3);
	// for(int i=0;i<100;i++){
	// //HighVoltageSwitch a1=SwitchService.selectByPrimaryKey(i+"");
	// if(i%2==0){
	// ActiveHighSwitch active3 = new ActiveHighSwitch();
	// active3.setOpen(true);
	// active3.setStatus("01");
	// active3.setHitchEventId(null);
	// active3.setId(i+"");
	// list.add(active3);
	// }else{
	// ActiveHighSwitch active3 = new ActiveHighSwitch();
	// active3.setOpen(false);
	// active3.setStatus("00");
	// active3.setHitchEventId(null);
	// active3.setId(i+"");
	// list.add(active3);
	// }
	// }
	//// ActiveHighSwitch active2 = new ActiveHighSwitch();
	//// active2.setOpen(true);
	//// active2.setStatus("00");
	//// active2.setHitchEventId(null);
	//// active2.setId("25a4d5f3752443c78e2dfe6189704e95");
	//// list.add(active2);
	// this.switches.addAll(list);
	// }

	@RequestMapping("/control_switch")
	@ResponseBody
	public String controlSwitch(@RequestParam(required = true) String switchId, int sign, int type,
			HttpSession session) {
		String address = hardwareClient.getService().getOnlineAddressById(switchId);
		String msg = null;
		User user = (User) session.getAttribute("currentUser");
		if (sign == 0) { // 分闸
			logger.info("用户 " + user.getName() + " 对地址为" + address + "的设备执行分闸");
			msg = hardwareClient.getService().generateOpenSwitchMessage(address, type);
		} else { // 合闸
			logger.info("用户 " + user.getName() + " 对地址为" + address + "的设备执行合闸");
			msg = hardwareClient.getService().generateCloseSwitchMessage(address, type);
		}
		oLogService.createNewOperationLog(user.getId(), type, switchId);
		// 发送报文
		if (msg != null) {

			logger.info("type为" + type + "，address为" + address + "，发送开合闸报文：" + msg);
		} else {
			return "error";
		}
		return "success";
	}

	// @RequestMapping("/test_control")
	// @ResponseBody
	// public String testControl(@RequestParam(required = true) String switchId,
	// int sign, int type, HttpSession session) {
	// String address =
	// hardwareClient.getService().getOnlineAddressById(switchId);
	// String msg = null;
	// if(sign == 0) { //开
	// msg = hardwareClient.getService().generateOpenSwitchMessage(address,
	// type);
	// //msg = hardwareClient.getService().generateOpenSwitchMessage(address);
	// } else { //合
	// msg = hardwareClient.getService().generateCloseSwitchMessage(address,
	// type);
	// }
	// User user = (User)session.getAttribute("currentUser");
	// oLogService.createNewOperationLog(user.getId(), type, switchId);
	// // 发送报文
	// if (msg != null) {
	//
	// logger.info("发送开合闸报文" + msg);
	// } else {
	// return "error";
	// }
	// return "success";
	// }

	/*
	 * 未来改进的方法，删除control_switch这一个接口
	 */
	// private ResponseMessage controlSwitch(
	// @RequestParam(required = true) String switchId, int sign, int type, User
	// user) {
	// String address =
	// hardwareClient.getService().getOnlineAddressById(switchId);
	// String msg = null;
	// if(sign == 0) { //开
	// msg = hardwareClient.getService().generateOpenSwitchMessage(address,
	// type);
	// //msg = hardwareClient.getService().generateOpenSwitchMessage(address);
	// } else { //合
	// msg = hardwareClient.getService().generateCloseSwitchMessage(address,
	// type);
	// }
	// oLogService.createNewOperationLog(user.getId(), type, switchId);
	// // 发送报文
	// if (msg != null) {
	//
	// logger.info("发送开合闸报文" + msg);
	// } else {
	// return ResponseMessage.danger("系统错误：开关闸报文获取失败");
	// }
	// return ResponseMessage.success("操作成功");
	// }

	@RequestMapping("/read_voltage")
	@ResponseBody
	public void readVoltage(@RequestParam(required = true) String switchId, String type, HttpSession session) {

		User user = null;
		if (session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		} else {
			return;
		}
		/*
		 * MsgPushThreadManager.createScheduledPoolDaemonThread(
		 * getVoltageRunnable(user.getName(), type, switchId), 6, user.getId());
		 */
		template.convertAndSendToUser(user.getName(), "/queue/read_voltage",
				deviceCommonService.getVoltageService(Integer.valueOf(type)).getVoltage(switchId));
	}

	@RequestMapping("/stop_read_param")
	@ResponseBody
	public void stopReadParam(HttpSession session) {

		if (session.getAttribute("currentUser") != null) {
			DeviceBinding.unbinding((User) session.getAttribute("currentUser"));
		}
	}

	/**
	 * TODO
	 */
	/*
	 * private Runnable getVoltageRunnable(final String userName, final String
	 * type, final String switchId) { return new Runnable() { public void run()
	 * { // template.convertAndSendToUser(userName, "/queue/read_voltage", //
	 * getVoltage(type, switchId)); template.convertAndSendToUser(userName,
	 * "/queue/read_voltage", <<<<<<< HEAD getVoltVisual()); =======
	 * getVoltage(type, switchId));
	 *//*
		 * template.convertAndSendToUser(userName, "/queue/read_voltage",
		 * getVoltVisual());
		 *//*
		 * >>>>>>> a9afadf4bca524dd905f09926d1a3216010fc8b2 } }; }
		 */
	/*
	 * private Integer[] getVoltage(String type, String switchId) { Integer[]
	 * deStrings = new Integer[3]; switch (type) { case "0":// 低压开关 // 从数据库中查询结果
	 * List<LowVoltageVoltage> cliList = voltageService .getRecentlyVoltage();
	 * 
	 * if (cliList != null) { for (LowVoltageVoltage current : cliList) { String
	 * p = current.getPhase(); switch (p) { case "A": deStrings[0] =
	 * current.getValue(); break; case "B": deStrings[1] = current.getValue();
	 * break; case "C": deStrings[2] = current.getValue(); break; default:
	 * break; } } } break; case "1":// 高压开关 // 从数据库中查询结果
	 * List<HighVoltageVoltage> cliList2 = voltageService2
	 * .getRecentlyVoltage(switchId, "A"); if (cliList2 != null &&
	 * cliList2.size() != 0) { deStrings[0] = cliList2.get(0).getValue(); }
	 * 
	 * cliList2 = voltageService2 .getRecentlyVoltage(switchId, "B"); if
	 * (cliList2 != null && cliList2.size() != 0) { deStrings[1] =
	 * cliList2.get(0).getValue(); } cliList2 = voltageService2
	 * .getRecentlyVoltage(switchId, "C"); if (cliList2 != null &&
	 * cliList2.size() != 0) { deStrings[2] = cliList2.get(0).getValue(); }
	 * break; case "2":// 管控开关 // 从数据库中查询结果 List<ControlMearsureVoltage>
	 * cliList3 = voltageService3 .getRecentlyVoltage();
	 * 
	 * if (cliList3 != null) { for (ControlMearsureVoltage current : cliList3) {
	 * String p = current.getPhase(); switch (p) { case "A": deStrings[0] =
	 * current.getValue(); break; case "B": deStrings[1] = current.getValue();
	 * break; case "C": deStrings[2] = current.getValue(); break; default:
	 * break; } }
	 * 
	 * } break; default: break; } return deStrings; }
	 */
	/**
	 * 通过前端发送请求，获取开关的id，从而读取开关电压之，再根据socket的订阅，定点推送到特定的人
	 */
	@RequestMapping("/read_current")
	@ResponseBody
	public void readCurrentVariable(@RequestParam(required = true) final String switchId, final String type,
			HttpSession session) {

		User user = null;
		if (session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		}
		List<Integer> list = deviceCommonService.getCurrentService(Integer.valueOf(type)).readCurrent(switchId);
		template.convertAndSendToUser(user.getName(), "/queue/read_current", 
				currentService.getRealCurrent(switchId, list));
	}

	/**
	 * TODO
	 */
	/*
	 * private Runnable getCurrentRunnable(final String userName, final String
	 * type, final String switchId) { return new Runnable() { public void run()
	 * { // template.convertAndSendToUser(userName, "/queue/read_current", //
	 * getCurrent(type, switchId)); template.convertAndSendToUser(userName,
	 * "/queue/read_current", <<<<<<< HEAD getCurrVisual()); =======
	 * getCurrent(type, switchId));
	 */
	/*
	 * template.convertAndSendToUser(userName, "/queue/read_current",
	 * getCurrVisual());
	 *//*
		 * >>>>>>> a9afadf4bca524dd905f09926d1a3216010fc8b2 } }; }
		 */
	/*
	 * private Integer[] getCurrent(String type, String switchId) { Integer[]
	 * deStrings = new Integer[3]; switch (type) { case "0":// 低压开关 // 从数据库中查询结果
	 * List<LowVoltageCurrent> cliList = currentService .getRecentlyCurrent();
	 * 
	 * if (cliList != null) { for (LowVoltageCurrent current : cliList) { String
	 * p = current.getPhase(); switch (p) { case "A": deStrings[0] =
	 * current.getValue(); break; case "B": deStrings[1] = current.getValue();
	 * break; case "C": deStrings[2] = current.getValue(); break; default:
	 * break; } } } break; case "1":// 高压开关
	 * 
	 * break; case "2":// 管控开关 // 从数据库中查询结果 List<ControlMearsureCurrent>
	 * cliList3 = currentService3 .getRecentlyCurrent();
	 * 
	 * if (cliList3 != null) { for (ControlMearsureCurrent current : cliList3) {
	 * String p = current.getPhase(); switch (p) { case "A": deStrings[0] =
	 * current.getValue(); break; case "B": deStrings[1] = current.getValue();
	 * break; case "C": deStrings[2] = current.getValue(); break; default:
	 * break; } }
	 * 
	 * } break; default: break; } return deStrings; }
	 */

	@RequestMapping("/read_lvswitch_status")
	@ResponseBody
	public Object read_lvswitch_status(String id) {
		return hardwareClient.getService().getSwitchGPRS(id);
	}

	/**
	 * 
	 * @Title: read_hvswitch_status @Description: 高压开关状态 @param @param
	 * id @param @return @return Object @throws
	 */
	@RequestMapping("/read_hvswitch_status")
	@ResponseBody
	public void read_hvswitch_status(final String id, HttpSession session) {

		final User user;
		if (session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		} else {
			return;
		}

		HighVoltageStatus status = hardwareClient.getService().getStatusbyId(id);
		if (status != null) {
			template.convertAndSendToUser(user.getName(), "/queue/read_hv_status", status);
		}

		DeviceBinding.binding(user, id);
	}
	/*
	 * MsgPushThreadManager.createScheduledPoolDaemonThread( new Runnable() {
	 * 
	 * @Override public void run() { try {
	 * 
	 * //TODO <<<<<<< HEAD // HighVoltageStatus status =
	 * hardwareService.getStatusbyId(id); // if(status != null) { //
	 * template.convertAndSendToUser(user.getName(), // "/queue/read_hv_status",
	 * status); // } template.convertAndSendToUser(user.getName(),
	 * "/queue/read_hv_status", getStatusVisual()); ======= HighVoltageStatus
	 * status = hardwareClient.getService().getStatusbyId(id); if(status !=
	 * null) { template.convertAndSendToUser(user.getName(),
	 * "/queue/read_hv_status", status); }
	 *//*
		 * template.convertAndSendToUser(user.getName(),
		 * "/queue/read_hv_status", getStatusVisual());
		 *//*
		 * >>>>>>> a9afadf4bca524dd905f09926d1a3216010fc8b2 } catch
		 * (MessagingException e1) { e1.printStackTrace(); } } }, 6,
		 * user.getId());
		 * 
		 * }
		 */

	// **************************************
	// TODO 虚拟数据获取
	// **************************************

	public HighVoltageStatus getStatusVisual() {

		int i = new Random().nextInt(4);
		if (i == 1) {
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
		} else if (i == 2) {
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
		if (i == 1) {
			return new Integer[] { 345, 0, 0 };
		} else if (i == 2) {
			return new Integer[] { 166, 0, 0 };
		} else {
			return new Integer[] { 452, 0, 0 };
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

	/**
	 * 开启实时监测报文模块
	 * @param switchId
	 * @param session
	 */
	@RequestMapping("/read_text")
	@ResponseBody
	public void readText(String switchId, HttpSession session) {

		final User user;
		if (session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		} else {
			return;
		}
		DeviceBinding.bindingMonitor(user, switchId);
		hardwareClient.getService().regisMonitor(switchId);
	}
	
	/**
	 * 退出实时监测报文模块
	 * @param switchId
	 * @param session
	 */
	@RequestMapping("/stop_read_text")
	@ResponseBody
	public void stopReadText(String switchId, HttpSession session) {

		final User user;
		if (session.getAttribute("currentUser") != null) {
			user = (User) session.getAttribute("currentUser");
		} else {
			return;
		}
		DeviceBinding.unbindingMonitor(user, switchId);
		List<User> monitorUser = DeviceBinding.getMonitorUser(switchId);
		if (null == monitorUser || monitorUser.size() == 0) {
			hardwareClient.getService().removeMonitor(switchId);
		}
	}

	/**
	 * 实时监测报文模块发送报文
	 * @param switchId
	 * @param text
	 * @param session
	 */
	@RequestMapping("/send_text")
	@ResponseBody
	public boolean sendText(String switchId, String text, HttpSession session) {
		return hardwareClient.getService().sendText(switchId, text, 0);
	}
	
	/**
	 * 实时监测报文模块发送报文
	 * @param switchId
	 * @param text
	 * @param session
	 */
	@RequestMapping("/send_total_call")
	@ResponseBody
	public boolean sendTotalCall(String switchId, HttpSession session) {
		return hardwareClient.getService().sendText(switchId, null, 1);
	}
	
	@RequestMapping("/send_total_call_anon")
	@ResponseBody
	public boolean sendTotalCallAnon(String switchId, HttpSession session) {
		return hardwareClient.getService().sendText(switchId, null, 2);
	}
	

}
