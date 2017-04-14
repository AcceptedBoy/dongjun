package com.gdut.dongjun.web;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.BigGroup;
import com.gdut.dongjun.domain.vo.HitchEventVO;
import com.gdut.dongjun.service.BigGroupService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.temperature.TemperatureMeasureService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.service.webservice.server.WebsiteService;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/test")
public class TestController implements InitializingBean {

//	@Autowired
//	private MQProductHelper mqHelper;
	@Autowired
	private UserService userService;
	@Autowired
	private WebsiteService webService;
	@Autowired
	private HardwareServiceClient hardService;
	@Autowired
	private TemperatureMeasureService measureService;
	
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
//						Thread.sleep(1000 * 5);
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
	
	@RequestMapping("/temtest")
	@ResponseBody
	public ResponseMessage temtest() {
		hardService.getService().changeTemperatureDevice(1 + "");
		return ResponseMessage.success("fucked!");
	}
	
	@Autowired
	private BigGroupService service;
	@RequestMapping("/grouptest")
	@ResponseBody
	public ResponseMessage grouptest() {
		BigGroup g = new BigGroup();
		g.setId(UUIDUtil.getUUID());
		g.setIsDefault(1);
		g.setName("test name");
		service.updateByPrimaryKey(g);
		return ResponseMessage.success("success");
	}
	
	@RequestMapping("/grouptest1")
	@ResponseBody
	public ResponseMessage grouptes1t() {
		BigGroup g = service.selectByPrimaryKey("3");
		g.setName("test name1111");
		service.updateByPrimaryKey(g);
		return ResponseMessage.success("success");
	}
	
	@RequestMapping("/grouptest2")
	@ResponseBody
	public ResponseMessage group2tes1t() {
		BigGroup g = new BigGroup();
		g.setId("3");
		g.setName("test name122222");
		service.updateByPrimaryKeySelective(g);
		return ResponseMessage.success("success");
	}
	
	
	@RequestMapping("/updatetime")
	@ResponseBody
	public ResponseMessage update() {
//		int count = measureService.getCount();
////		int part = count / 24;
//		Calendar cal = Calendar.getInstance();
////		long nano = System.currentTimeMillis();
//		for (int i = 0; i < count; i++) {
//			cal.add(Calendar.MINUTE, -10);
//			Date date = cal.getTime();
//			int pre = i;
//			int after = 1;
//			System.out.println(count - i);
//			measureService.updateTime(pre, after, date);
//		}
		String[] list = {
				"489e2a322b6d4a7782debb5e1cf7cc8b",
				"518b5ed8f378440c9e2c876fc634265a",
				"ba0a31c8171d4b18ae1ddc33da89ca05",
				"cb745bea791d440c82e6feedff46422f",
				"1c4a4d2a4ae64a2e9181eab971b483d8"
		};
		for (String s : list) {
			submitTask(s);
		}
		return ResponseMessage.success("succes!");
	}
	
	public void submitTask(final String deviceId) {
		new Thread() {

			@Override
			public void run() {
				int count = measureService.getCount(deviceId);
				Calendar cal = Calendar.getInstance();
				for (int i = 0; i < count; i++) {
					cal.add(Calendar.MINUTE, -10);
					Date date = cal.getTime();
					int pre = i;
					int after = 1;
					measureService.updateTime(pre, after, deviceId, date);
				}
			}
			
		}.start();
	}
	
}
