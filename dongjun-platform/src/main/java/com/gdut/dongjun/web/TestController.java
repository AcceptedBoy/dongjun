package com.gdut.dongjun.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.webservice.server.WebsiteService;

@Controller
@RequestMapping("/test")
public class TestController implements InitializingBean {

//	@Autowired
//	private MQProductHelper mqHelper;
	@Autowired
	private UserService userService;
	@Autowired
	private WebsiteService webService;
	
	private Logger logger = Logger.getLogger(TestController.class);	
	
//	@RequestMapping("/send")
//	@ResponseBody
//	public ResponseMessage send() {
//		HitchEventVO vo = new HitchEventVO();
//		vo.setGroupId("1");
//		vo.setId("03cb2cdd6f364427bc4d681efd2d3ce3");
//		vo.setSwitchId("1");
//		vo.setType(3);
//		User user = userService.selectByPrimaryKey("001");
//		try {
//			mqHelper.sendHitchEvent(user, vo);
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
//		return ResponseMessage.success("fuck");
//	}
	
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

	@Override
	public void afterPropertiesSet() throws Exception {
//		HitchEventVO vo = new HitchEventVO();
//		vo.setGroupId("1");
//		vo.setId("03cb2cdd6f364427bc4d681efd2d3ce3");
//		vo.setSwitchId("1");
//		vo.setType(3);
//		/*
//		 * 没事报个警，如果mq运行正确，那么我发报警，
//		 * mq收到，用户登录，访问test/MQTest.html，控制台收到报警信息。
//		 * 或者用户登录，发报警，控制台也会收到报警消息。
//		 */
//		webService.callbackHitchEvent(vo);
//		webService.callbackHitchEvent(vo);
//		webService.callbackHitchEvent(vo);
		
//		Thread t = new Thread() {
//
//			@Override
//			public void run() {
//				while (true) {
//					logger.info("我随便打印了点消息");
//					//睡眠三十分钟
//					try {
//						Thread.sleep(1000 * 1);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			
//		};
//		t.start();
	}
}
