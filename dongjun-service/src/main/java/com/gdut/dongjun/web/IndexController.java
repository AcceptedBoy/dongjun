package com.gdut.dongjun.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/dongjun/login")
	public String getLoginPage() {
		return "login";
	}
	
	@RequestMapping("/dongjun/companyManager")
	public String companyManager() {
		return "companyManager";
	}
	
	@RequestMapping("/dongjun/switchManager")
	public String switchManager() {
		return "switchManager";
	}
	
	@RequestMapping("/testAvailable")
	public void testAvailable(HttpServletResponse response) throws IOException {
		PrintWriter w = response.getWriter();
		w.write("OK");
		w.flush();
		return ;
	}
}
