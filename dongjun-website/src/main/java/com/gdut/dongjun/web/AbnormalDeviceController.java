package com.gdut.dongjun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.service.AbnormalDeviceService;
import com.gdut.dongjun.service.device.HighVoltageSwitchService;

@Controller
@RequestMapping("/dongjun/abnormal_device")
public class AbnormalDeviceController {

	@Autowired
	private AbnormalDeviceService abService;
	
	@Autowired
	private HighVoltageSwitchService switchService;
	
	@RequestMapping("/add")
	@ResponseBody
	public String addDevice() {
		//TODO
		return null;
	}
	
}
