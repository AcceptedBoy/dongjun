package com.gdut.dongjun.service.webservice.server.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.dto.HitchEventDTO;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.common.DeviceBinding;
import com.gdut.dongjun.service.device.DeviceCommonService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.device.LowVoltageSwitchService;
import com.gdut.dongjun.service.device.TemperatureDeviceService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.server.WebsiteService;
import com.gdut.dongjun.util.GenericUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 */
@Component
public class WebsiteServiceImpl implements WebsiteService {

	private static final Logger LOG = LoggerFactory.getLogger(WebsiteServiceImpl.class);

	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private DeviceCommonService deviceCommonService;
	@Autowired
	private HardwareServiceClient hardwareClient;
	@Autowired
	private UserService userService;
	@Autowired
	private HighVoltageSwitchService highService;
	@Autowired
	private LowVoltageSwitchService lowService;
	@Autowired
	private TemperatureDeviceService temService;

	@Autowired
	public WebsiteServiceImpl(SimpMessagingTemplate messagingTemplate) {
		this.template = messagingTemplate;
	}

	@Override
	public void callbackCtxChange(List<ActiveHighSwitch> data) {
		LOG.info("hardware端ctx变化，进行页面推送");
		// this.template.convertAndSend("/topic/get_active_switch_status",
		// data);

	}

	@Override
	public void callbackDeviceChange(String switchId, Integer type) {
		LOG.info("设备状态发生变化" + switchId + "    type为" + type);
		List<User> userList = DeviceBinding.getListenUser(switchId);
		if (!CollectionUtils.isEmpty(userList)) {
			for (User user : userList) {

				template.convertAndSendToUser(user.getName(), "/queue/read_current",
						deviceCommonService.getCurrentService(Integer.valueOf(type)).readCurrent(switchId));

				template.convertAndSendToUser(user.getName(), "/queue/read_voltage",
						deviceCommonService.getVoltageService(Integer.valueOf(type)).getVoltage(switchId));

				HighVoltageStatus status = hardwareClient.getService().getStatusbyId(switchId);
				if (status != null) {
					template.convertAndSendToUser(user.getName(), "/queue/read_hv_status", status);
				}
			}
		}
	}

	@Override
	public void callbackHitchEvent(HitchEventVO event) {
		LOG.info("报警信息到达，报警设备为" + event.getSwitchId() + "    报警类型为" + event.getType());
		HitchEventDTO dto = wrapDTO(event);
		List<User> userList = userService.selectByParameters(MyBatisMapUtil.warp("company_id", event.getGroupId()));
		if (!CollectionUtils.isEmpty(userList)) {
			for (User user : userList) {
				template.convertAndSendToUser(user.getName(), "/queue/subscribe_hitch_event", dto);
			}
		}
	}

	public HitchEventDTO wrapDTO(HitchEventVO vo) {
		HitchEventDTO dto = new HitchEventDTO();
		Object obj = null;
		switch (vo.getType()) {
		case 0: {
			obj = lowService.selectByPrimaryKey(vo.getSwitchId());
			break;
		}
		case 1: {
			obj = highService.selectByPrimaryKey(vo.getSwitchId());
			break;
		}
		case 2: {
			//TODO
		}
		case 3: {
			obj = temService.selectByPrimaryKey(vo.getSwitchId());
			break;
		}
		default: {
			LOG.info("HitchEventVO数据异常");
			break;
		}
		}
		dto.setGroupId(vo.getGroupId());
		dto.setHitchReason(vo.getHitchReason());
		dto.setHitchTime(vo.getHitchTime());
		dto.setType(returnType(vo.getType()));
		dto.setName((String)GenericUtil.getPrivateObjectValue(obj, "name"));
		return dto;
	}
	
	/**
	 * TODO
	 * 以后变成一个const
	 */
	public String returnType(Integer i) {
		switch (i) {
		case 1 : return "低压设备";
		case 2 : return "管控设备";
		case 3 : return "温度设备";
		default : return "";
		}
	}
}
