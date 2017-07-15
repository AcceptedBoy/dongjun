package com.gdut.dongjun.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.web.vo.ResponseMessage;

@RequestMapping("/dongjun")
@Controller
public class UserController {

	@ResponseBody
	@RequestMapping("/login")
	public ResponseMessage login(String name, String password, HttpSession session) {
		
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/logout")
	public ResponseMessage logout(String name, String password, HttpSession session) {
		
		return null;
	}
	
	
	
}
