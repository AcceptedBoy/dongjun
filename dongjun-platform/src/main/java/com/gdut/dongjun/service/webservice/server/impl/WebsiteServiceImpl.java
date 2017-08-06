package com.gdut.dongjun.service.webservice.server.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gdut.dongjun.domain.dto.ActiveHighSwitch;
import com.gdut.dongjun.domain.dto.HitchEventDTO;
import com.gdut.dongjun.domain.po.PersistentHitchMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.PersistentHitchMessageService;
import com.gdut.dongjun.service.RemoteEventService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.webservice.client.po.InfoEventDTO;
import com.gdut.dongjun.service.webservice.server.WebsiteService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;
import com.gdut.dongjun.web.vo.HitchEventVO;
import com.gdut.dongjun.web.vo.InfoEventVO;

/**
 */
@Component
public class WebsiteServiceImpl implements WebsiteService {

	private static final Logger LOG = LoggerFactory.getLogger(WebsiteServiceImpl.class);

	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private UserService userService;
	@Autowired
	private RemoteEventService remoteEventService;
	@Autowired
	private PersistentHitchMessageService persistentMessageService;
	@Autowired
	public WebsiteServiceImpl(SimpMessagingTemplate messagingTemplate) {
		this.template = messagingTemplate;
	}

	@Override
	public void callbackCtxChange(List<ActiveHighSwitch> data) {}

	@Override
	public void callbackDeviceChange(String switchId, Integer type) {}

	@Override
	public void callbackHitchEvent(HitchEventDTO event) {
		LOG.info("报警信息到达，报警设备为" + event.getModuleId() + "    报警类型为" + event.getType());
		HitchEventVO dto = remoteEventService.wrapHitchVO(event);
		List<User> userList = userService.selectByParameters(MyBatisMapUtil.warp("company_id", event.getCompanyId()));
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
//					msg.setGroupId(event.getGroupId());
					msg.setCompanyId(event.getCompanyId());
					msg.setHitchId(event.getId());
//					msg.setMonitorId(event.getMonitorId());
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

	@Override
	public void callbackInfoEvent(InfoEventDTO event) {
		LOG.info("通知信息到达，信息类型为" + event.getType());
		InfoEventVO vo = remoteEventService.wrapIntoVO(event);
		List<User> userList = userService.selectByParameters(MyBatisMapUtil.warp("company_id", event.getGroupId()));
		for (User user : userList) {
			if (userService.isUserOnline(user.getId())) {
				template.convertAndSend("/queue/user-" + user.getId() + "/info", vo);
			} 
//			else {
//				//信息持久化
//				
//			}
		}
		
		
	}

}
