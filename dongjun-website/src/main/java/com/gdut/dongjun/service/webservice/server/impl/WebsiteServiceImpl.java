package com.gdut.dongjun.service.webservice.server.impl;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.domain.HighVoltageStatus;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.service.common.DeviceBinding;
import com.gdut.dongjun.service.device.DeviceCommonService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;
import com.gdut.dongjun.service.device.current.HighVoltageCurrentService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.server.WebsiteService;
import com.gdut.dongjun.util.VoiceFixUtil;

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
	private HighVoltageCurrentService currentService;
	@Autowired
	private HighVoltageSwitchService switchService;

	@Autowired
	public WebsiteServiceImpl(SimpMessagingTemplate messagingTemplate) {
		this.template = messagingTemplate;
	}

	@Override
	public void callbackCtxChange(List<ActiveHighSwitch> data) {
		LOG.info("hardware端ctx变化，进行页面推送");
		this.template.convertAndSend("/topic/get_active_switch_status", data);
	}

	@Override
	public void callbackDeviceChange(String switchId, Integer type) {
		LOG.info("设备状态发生变化" + switchId + "    type为" + type);
		List<User> userList = DeviceBinding.getListenUser(switchId);
		if (!CollectionUtils.isEmpty(userList)) {
			for (User user : userList) {
				List<Integer> list = deviceCommonService.getCurrentService(Integer.valueOf(type)).readCurrent(switchId);
				template.convertAndSendToUser(user.getName(), "/queue/read_current",
						currentService.getRealCurrent(switchId, list));

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
	public void callbackTextArrived(String switchId, String text) {
		LOG.info("实时监测设备" + switchId + "报文到达：" + text);
		List<User> list = DeviceBinding.getMonitorUser(switchId);
		if (!CollectionUtils.isEmpty(list)) {
			for (User user : list) {
				template.convertAndSendToUser(user.getName(), "/queue/read_text", text);
			}
		}
	}

	@Override
	public void callbackCtxChangeForVoice(String id, int type) {
		if (null == id) {
			return;
		}
		// 语音提示
		String name = switchService.selectByPrimaryKey(id).getName();
		// String name = "测试设备";
		byte[] array = null;
		String logName = null;
		if (type == 0) {
			array = VoiceFixUtil.request2(name + "执行分闸");
			logName = name + "执行分闸";
		} else if (type == 1) {
			array = VoiceFixUtil.request2(name + "执行合闸");
			logName = name + "执行合闸";
		} else if (type == 2) {
			// array = VoiceFixUtil.request2(name + "上线");
			return; // 暂时设置上线没有语音提示
		} else if (type == 3) {
			array = VoiceFixUtil.request2(name + "下线");
			logName = name + "下线";
		}
		LOG.info(logName);
		byte[] encodeBase64 = Base64.encodeBase64(array);
		this.template.convertAndSend("/topic/switch_event", new String(encodeBase64));
	}
}
