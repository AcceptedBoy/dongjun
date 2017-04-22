package com.gdut.dongjun.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.ZTreeNodeService;
import com.gdut.dongjun.service.cache.CacheService;
import com.gdut.dongjun.service.common.CommonSwitch;
import com.gdut.dongjun.service.webservice.client.CentorServiceClient;
import com.gdut.dongjun.util.EncoderUtil;
import com.gdut.dongjun.util.VoiceFixUtil;

/**
 * 负责返回树状数据、控制重定向、报警信息的类，实现{@code InitializingBean}能够在初始化之后执行{@code afterPropertiesSet()}
 * @author Gordan_Deng
 * @date 2017年2月18日
 */
@Controller
@RequestMapping("/dongjun")
@SessionAttributes("currentUser")
public class IndexController implements InitializingBean {

	@Autowired
	private ZTreeNodeService zTreeNodeService;

	@Autowired
	private CommonSwitch commonSwitch;

	@Autowired
	private CentorServiceClient centorServiceClient;
	
	@Resource(name="EhCacheService")
	private CacheService ehCacheService;
	
	@Autowired
	private CompanyService companyService;

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
	/*
	 * 这是一进入index界面会访问的方法，不需要type
	 */
	@RequiresAuthentication
	@RequestMapping("/switch_tree")
	@ResponseBody
	public Object switchTree(@RequestParam(required = false) String type,
			Model model, HttpSession session) {
//			RedirectAttributes redirectAttributes) {

		User user = (User) session.getAttribute("currentUser");
		if (user != null && user.getCompanyId() != null) {
			if(commonSwitch.canService()) {

				return centorServiceClient.getService()
						.getSwitchTree(user.getCompanyId(), type);
			}	//中介者
			//TODO 以后这个方法会变成中介者系统返回值
			return zTreeNodeService.getSwitchTree(user.getId());
		} else {
			//超管
			return zTreeNodeService.getSwitchTree();
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

	@RequiresAuthentication
	@RequestMapping("get_voice_of_event")
	@ResponseBody
	public String getVoiceOfEvent(@RequestParam(required=false) String name) {
		
		String httpUrl = "http://apis.baidu.com/apistore/baidutts/tts";
		String httpArg = "text=" + EncoderUtil.getUrlEncode
				("开关" + name + "已经报警，请及时处理") +"&ctp=1&per=0";
		return VoiceFixUtil.request(httpUrl, httpArg);
	}

	/**
	 * 系统启动后缓存每个公司的设备树状图
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
//		List<Company> allCompany = companyService.selectByParameters(null);
//		for (Company c : allCompany) {
//			companyService.updateChartCache(c.getId());
//		}
	}

}
