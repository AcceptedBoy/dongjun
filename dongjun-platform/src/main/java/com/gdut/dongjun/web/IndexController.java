package com.gdut.dongjun.web;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.ZTreeNodeService;
import com.gdut.dongjun.util.EncoderUtil;
import com.gdut.dongjun.util.VoiceFixUtil;
import com.gdut.dongjun.webservice.Constant;
import com.gdut.dongjun.webservice.client.CommonServiceClient;
import com.gdut.dongjun.webservice.util.JaxrsClientUtil;
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
@RequestMapping("/dongjun")
@SessionAttributes("currentUser")
public class IndexController {

	@Autowired
	private ZTreeNodeService zTreeNodeService;

	@Autowired
	private Constant constant;

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
	 * symon 获取分组接口
     */
	@RequestMapping("/group_tree")
	@ResponseBody
	public Object groupTree(String companyId, Integer deviceType) {
		return zTreeNodeService.groupTree(companyId, deviceType);
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

	@RequestMapping("get_voice_of_event")
	@ResponseBody
	public String getVoiceOfEvent(@RequestParam(required=false) String name) {
		
		String httpUrl = "http://apis.baidu.com/apistore/baidutts/tts";
		String httpArg = "text=" + EncoderUtil.getUrlEncode
				("开关" + name + "已经报警，请及时处理") +"&ctp=1&per=0";
		return VoiceFixUtil.request(httpUrl, httpArg);
	}
}
