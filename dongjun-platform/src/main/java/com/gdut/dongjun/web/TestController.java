package com.gdut.dongjun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.service.webservice.server.WebsiteService;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	private WebsiteService webService;
	
	@RequestMapping("/testpush")
	@ResponseBody
	
	public String back() {
		webService.callbackDeviceChange("1", 3);
		return "success";
	}
}
