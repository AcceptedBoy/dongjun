package com.gdut.dongjun.web;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.dto.HitchEventDTO;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.manager.MQProductHelper;

//@Controller
//@RequestMapping("/test")
public class TestController {

	@Autowired
	private MQProductHelper mqHelper;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/send")
	@ResponseBody
	public ResponseMessage send() {
		HitchEventDTO dto = new HitchEventDTO();
		dto.setHitchTime("1996-08-07 12:12:12");
		dto.setGroupId("1");
		dto.setHitchReason("大概系统傻了吧");
		dto.setType("温度设备");
		dto.setName("大概是温度设备");
		User user = userService.selectByPrimaryKey("001");
		try {
			mqHelper.sendHitchEvent(user, dto);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return ResponseMessage.success("fuck");
	}
}
