package com.gdut.dongjun.service.webservice.server.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.domain.dto.ActiveHighSwitch;
import com.gdut.dongjun.domain.dto.DeviceOnlineDTO;
import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.domain.model.SubcribeResponseMessage;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.PersistentHitchMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.RemoteEventService;
import com.gdut.dongjun.service.PersistentHitchMessageService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.mq.UserMQService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.client.po.InfoEventDTO;
import com.gdut.dongjun.service.webservice.server.WebsiteService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;
import com.gdut.dongjun.web.vo.DeviceOnlineVO;
import com.gdut.dongjun.web.vo.HitchEventVO;

/**
 */
@Component
public class WebsiteServiceImpl implements WebsiteService {

	private static final Logger LOG = LoggerFactory.getLogger(WebsiteServiceImpl.class);

	@Autowired
	private SimpMessagingTemplate template;
//	@Autowired
//	private DeviceCommonService deviceCommonService;
	@Autowired
	private HardwareServiceClient hardwareClient;
	@Autowired
	private UserService userService;
	@Autowired
	private RemoteEventService remoteEventService;
	@Autowired
	private PersistentHitchMessageService persistentMessageService;
	@Autowired
	private UserMQService mqService;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;
	@Autowired
	private DataMonitorService monitorService;
	@Autowired
	private TemperatureModuleService temModuleService;
	
	@Autowired
	public WebsiteServiceImpl(SimpMessagingTemplate messagingTemplate) {
		this.template = messagingTemplate;
	}

	@Override
	public void callbackCtxChange(List<ActiveHighSwitch> data) {}

	@Override
	public void callbackDeviceChange(String switchId, Integer type) {}

//	@Override
//	public void callbackDeviceChange(String switchId, Integer type) {
//		LOG.info("设备状态发生变化" + switchId + "    type为" + type);
//		List<User> userList = DeviceBinding.getListenUser(switchId);
//		if (!CollectionUtils.isEmpty(userList)) {
//			for (User user : userList) {
//				
//				template.convertAndSendToUser(user.getName(), "/queue/read_current",
//						deviceCommonService.getCurrentService(Integer.valueOf(type)).readCurrent(switchId));
//
//				template.convertAndSendToUser(user.getName(), "/queue/read_voltage",
//						deviceCommonService.getVoltageService(Integer.valueOf(type)).getVoltage(switchId));
//
//				HighVoltageStatus status = hardwareClient.getService().getStatusbyId(switchId);
//				if (status != null) {
//					template.convertAndSendToUser(user.getName(), "/queue/read_hv_status", status);
//				}
//			}
//		}
//	}

	@Override
	public void callbackHitchEvent(HitchEventDTO event) {
		LOG.info("报警信息到达，报警设备为" + event.getMonitorId() + "    报警类型为" + event.getType());
		HitchEventVO dto = remoteEventService.wrapIntoVO(event);
		List<User> userList = userService.selectByParameters(MyBatisMapUtil.warp("company_id", event.getGroupId()));
		if (!CollectionUtils.isEmpty(userList)) {
			for (User user : userList) {
				if (userService.isUserOnline(user.getId())) {
					//用户在线就直接推送
//					template.convertAndSendToUser(user.getName(), "/queue/subscribe_hitch_event", dto);
					template.convertAndSend("/queue/user-" + user.getId() + "/hitch", dto);
				} else {
//					//用户不在线就将消息持久化，等用户上线时再推送
					PersistentHitchMessage msg = new PersistentHitchMessage();
					msg.setId(UUIDUtil.getUUID());
					msg.setGroupId(event.getGroupId());
					msg.setHitchId(event.getId());
					msg.setMonitorId(event.getMonitorId());
					msg.setType(event.getType());
					msg.setUserId(user.getId());
					persistentMessageService.updateByPrimaryKey(msg);
					//mq功能尚有bug，不开放
//					try {
//						mqService.sendHitchMessage(user, dto);
//					} catch (JMSException e) {
//						//TODO 发生了错误之后启用原来的消息本地持久化做法
//						LOG.info("报警消息推送到MQ时发生错误，报错队列为/queue/user-" + user.getId() + "/hitch");
//						e.printStackTrace();
//						PersistentHitchMessage message = new PersistentHitchMessage(UUIDUtil.getUUID(), 
//								event.getType(), event.getId(), new Date(), user.getId(), new Date(), new Date());
//						persistentMessageService.updateByPrimaryKey(message);
//					}
				}
			}
		}
	}

//	@Override
//	public void callbackDeviceOnline(DeviceOnlineDTO event) {
////		LOG.info(event.getId() + "设备状态变动为" + event.getStatus());
//		DeviceOnlineDTO dto = new DeviceOnlineDTO();
//		
//		SubcribeResponseMessage message = new SubcribeResponseMessage();
//		message.setDate(event.getDate());
//		message.setMessageType(event.getDeviceType());
//		
//		List<DataMonitorSubmodule> submodules = submoduleService.selectByParameters(MyBatisMapUtil.warp("module_id", event.getId()));
//		String monitorId = submodules.get(0).getId();
//		DataMonitor monitor = monitorService.selectByPrimaryKey(monitorId);
//		dto.setMonitorName(monitor.getName());
//		dto.setType(event.getDeviceType());
//		dto.setDate(TimeUtil.timeFormat(event.getDate()));
//		dto.setStatus(event.getStatus());
//		message.setText(dto);
//		List<User> userList = userService.selectByParameters(MyBatisMapUtil.warp("company_id", monitor.getGroupId()));
//		if (!CollectionUtils.isEmpty(userList)) {
//			for (User user : userList) {
//				template.convertAndSend("/queue/user-" + user.getId() + "/hitch", message);
//			}
//		}
//	}

	@Override
	public void callbackInfoEvent(InfoEventDTO event) {
		LOG.info("通知信息到达，信息类型为" + event.getType());
		List<User> userList = userService.selectByParameters(MyBatisMapUtil.warp("company_id", event.getGroupId()));
		for (User user : userList) {
			if (userService.isUserOnline(user.getId())) {
				//TODO
				
				
			} else {
				//信息持久化
				
			}
		}
		
		
	}

}
