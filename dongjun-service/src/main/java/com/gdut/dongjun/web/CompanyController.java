package com.gdut.dongjun.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.web.vo.ResponseMessage;

@Controller
@RequestMapping("/dongjun/company")
public class CompanyController {
	
	@ResponseBody
	@RequestMapping("/list")
	public ResponseMessage list(String name, String password, HttpSession session) {
		
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(String name, String password, HttpSession session) {
		
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String name, String password, HttpSession session) {
		
		return null;
	}
	
	
	
	
}
