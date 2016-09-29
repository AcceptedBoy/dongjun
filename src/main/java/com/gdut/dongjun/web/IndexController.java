package com.gdut.dongjun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dongjun_service")
@SessionAttributes("currentUser")
public class IndexController {


	/**
	 * 
	 * @Title: forwardIndex
	 * @Description: TODO
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/index")
	public String forwardIndex() {
		return "index";
	}
	
	@RequestMapping("/characterPower")
	public String forwardCharacterPower() {
		return "characterPower";
	}

	@RequestMapping("/powerControl")
	public String forwardPowerControl() {
		return "powerControl";
	}

	@RequestMapping("/switchPower")
	public String forwardSwitchPower() {
		return "switchPower";
	}

	/**
	 * 
	 * @Title: manager
	 * @Description: TODO
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/manager")
	public String manager() {
		return "manager";
	}

}
