package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdut.dongjun.domain.po.Substation;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.SubstationService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

//@Controller
//@RequestMapping("/dongjun")
//@SessionAttributes("currentUser")
public class SubstationController {

	@Autowired
	private SubstationService substationService;
	
	private static final Logger logger = Logger
			.getLogger(SubstationController.class);

	/**
	 * 
	 * @Title: getLineSwitchList
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/substation_manager")
	public String getLineSwitchList(Model model) {

		model.addAttribute("switches",
				substationService.selectByParameters(null));
		return "substation_manager";
	}

	/**
	 * 
	 * @Title: getLineSwitchList
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/substation_list_by_company_id")
	@ResponseBody
	public Object getLineSwitchListByLineId(HttpSession session, Model model) {

		User user = (User) session.getAttribute("currentUser");
		String companyId = null;
		if (user != null) {

			companyId = user.getCompanyId();
		}
		List<Substation> lines = substationService
				.selectByParameters(MyBatisMapUtil
						.warp("company_id", companyId));

		int size = lines.size();
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp(
				"draw", 1);
		map.put("recordsTotal", size);
		map.put("data", lines);
		map.put("recordsFiltered", size);
		return map;
	}


	/**
	 * 删除特定的变电站，需要substation_admin:delete权限
	 */
	@RequestMapping("/del_substation")
	@ResponseBody
	//@RequiresPermissions("substation_admin:delete")
	public String delSwitch(@RequestParam(required = true) String switchId,
			Model model, RedirectAttributes redirectAttributes) {

		Substation substation = substationService.selectByPrimaryKey(switchId);
		if(substation != null) {
			try {
				substationService.deleteByPrimaryKey(switchId);// 删除这个开关
			} catch (Exception e) {
				logger.error("删除开关失败！");
				return null;
			}
			return substation.getCompanyId();
		} else {
			return null;
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
	@RequestMapping("/edit_substation")
	@ResponseBody
	//@RequiresPermissions("substation_admin:edit")
	public String editSwitch(Substation switch1, Model model,
			RedirectAttributes redirectAttributes) {

		if (switch1.getId() == "") {
			switch1.setId(UUIDUtil.getUUID());
		}
		try {

			substationService.updateByPrimaryKey(switch1);
		} catch (Exception e) {

			logger.error("修改变电站失败！");
			return null;
		}
		return switch1.getCompanyId();
	}

	@RequestMapping("/EventManage")
	public String eventManage(Model model) {

		return "EventManage";
	}
}
