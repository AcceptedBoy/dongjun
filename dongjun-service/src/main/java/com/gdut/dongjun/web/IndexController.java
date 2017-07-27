package com.gdut.dongjun.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dongjun")
public class IndexController {

	@RequestMapping("/login")
	public String getLoginPage() {
		return "login";
	}
}
