package com.gdut.dongjun.web;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.manager.MQProductHelper;
import com.gdut.dongjun.service.webservice.server.WebsiteService;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	private MQProductHelper mqHelper;
	@Autowired
	private UserService userService;
	@Autowired
	private WebsiteService webService;
	
	@RequestMapping("/send")
	@ResponseBody
	public ResponseMessage send() {
		HitchEventVO vo = new HitchEventVO();
		vo.setGroupId("1");
		vo.setId("03cb2cdd6f364427bc4d681efd2d3ce3");
		vo.setSwitchId("1");
		vo.setType(3);
		User user = userService.selectByPrimaryKey("001");
		try {
			mqHelper.sendHitchEvent(user, vo);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return ResponseMessage.success("fuck");
	}
	
	@RequestMapping("/send1")
	@ResponseBody
	public ResponseMessage send1() {
		HitchEventVO vo = new HitchEventVO();
		vo.setGroupId("1");
		vo.setId("03cb2cdd6f364427bc4d681efd2d3ce3");
		vo.setSwitchId("1");
		vo.setType(3);
		/*
		 * 没事报个警，如果mq运行正确，那么我发报警，
		 * mq收到，用户登录，访问test/MQTest.html，控制台收到报警信息。
		 * 或者用户登录，发报警，控制台也会收到报警消息。
		 */
		webService.callbackHitchEvent(vo);
		return ResponseMessage.success("fuck!");
	}
}
