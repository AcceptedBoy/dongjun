package com.gdut.dongjun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdut.dongjun.service.HighSwitchUserService;

@Controller
@RequestMapping("/dongjun")
public class HighSwitchUserController {

	@Autowired
	private HighSwitchUserService switchOperatorService;
	
	
}
