package com.gdut.dongjun.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.ZTreeNodeService;
import com.gdut.dongjun.service.common.CommonSwitch;
import com.gdut.dongjun.service.webservice.client.CommonServiceClient;
import com.gdut.dongjun.util.VoiceFixUtil;

@Controller
@RequestMapping("/dongjun")
@SessionAttributes("currentUser")
public class IndexController {

	@Autowired
	private ZTreeNodeService zTreeNodeService;

	@Autowired
	private CommonSwitch commonSwitch;

	@Autowired
	private CommonServiceClient centorServiceClient;

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
	
	@RequestMapping("/chart")
	public String forwardChart() {
		return "chart";
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

	/**
	 * 
	 * @Title: currentVoltageChart
	 * @Description: TODO
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/current_voltage_chart")
	public String currentVoltageChart() {
		return "current_voltage_chart";
	}
	
	/**
	 * 返回实时操控设备页面
	 * @return
	 */
	@RequestMapping("/chat")
	public String chat() {
		return "chat";
	}

	/**
	 * 
	 * @Title: switchTree
	 * @Description: TODO
	 * @param @param companyId
	 * @param @param model
	 * @param @param redirectAttributes
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/switch_tree")
	@ResponseBody
	public Object switchTree(@RequestParam(required = true) String type,
			Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {

		User user = (User) session.getAttribute("currentUser");
		if (user != null && user.getCompanyId() != null) {
//			if(commonSwitch.canService()) {
//
//				return centorServiceClient.getService()
//						.getSwitchTree(user.getCompanyId(), type);
//			}
			return zTreeNodeService.getSwitchTree(user.getCompanyId(), type);
		} else {

			return "";
		}
	}

	
	

	/**
	 * 
	 * @Title: editSwitch
	 * @Description: TODO
	 * @param @param switch1
	 * @param @param model
	 * @param @param redirectAttributes
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/switch_detail")
	public String enterMap(@RequestParam(required = true) String switchId,
			Model model) {

		model.addAttribute("switchId", switchId);
		return "switch_detail";
	}

	@RequestMapping("/get_voice_of_control")
	public ResponseEntity<byte[]> getVoiceOfEvent(@RequestParam(required=false) String name) throws IOException {
		byte[] a = VoiceFixUtil.request1(name);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", "testAudio.mp3");
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(a, headers, HttpStatus.OK);
	}
}
