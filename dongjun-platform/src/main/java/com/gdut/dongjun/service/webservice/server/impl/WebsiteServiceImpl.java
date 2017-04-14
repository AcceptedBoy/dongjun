package com.gdut.dongjun.service.webservice.server.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.domain.po.PersistentHitchMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.ActiveHighSwitch;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.dto.HitchEventDTO;
import com.gdut.dongjun.service.HitchEventService;
import com.gdut.dongjun.service.PersistentHitchMessageService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.mq.UserMQService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.server.WebsiteService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

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
	private HitchEventService hitchEventService;
	@Autowired
	private PersistentHitchMessageService persistentMessageService;
	@Autowired
	private UserMQService mqService;

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
	public void callbackHitchEvent(HitchEventVO event) {
		LOG.info("报警信息到达，报警设备为" + event.getSwitchId() + "    报警类型为" + event.getType());
		HitchEventDTO dto = hitchEventService.wrapIntoDTO(event);
		List<User> userList = userService.selectByParameters(MyBatisMapUtil.warp("company_id", event.getGroupId()));
		if (!CollectionUtils.isEmpty(userList)) {
			for (User user : userList) {
				if (userService.isUserOnline(user.getId())) {
					//用户在线就直接推送
//					template.convertAndSendToUser(user.getName(), "/queue/subscribe_hitch_event", dto);
					template.convertAndSend("/queue/user-" + user.getId() + "/hitch", dto);
				} else {
//					//用户不在线就将消息持久化，等用户上线时再推送
					PersistentHitchMessage message = new PersistentHitchMessage(UUIDUtil.getUUID(), 
							event.getType(), event.getId(), new Date(), user.getId());
					persistentMessageService.updateByPrimaryKey(message);
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

}
