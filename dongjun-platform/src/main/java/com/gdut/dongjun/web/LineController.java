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

import com.gdut.dongjun.domain.po.Line;
import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun")
@SessionAttributes("currentUser")
public class LineController {

	@Autowired
	private LineService lineService;

	private static final Logger logger = Logger.getLogger(LineController.class);

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
	@RequestMapping("/line_manager")
	public String getLineSwitchList(String lineId, Model model) {

		if (lineId != null) {

			model.addAttribute("switches", lineService
					.selectByParameters(MyBatisMapUtil.warp("line_id", lineId)));
		} else {
			model.addAttribute("switches", lineService.selectByParameters(null));
		}
		return "line_manager";
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
	@RequestMapping("/line_list_by_substation_id")
	@ResponseBody
	public Object getLineSwitchListByLineId(String substation_id,
			HttpSession session, Model model) {

		List<Line> lines = lineService.selectByParameters(MyBatisMapUtil.warp(
				"substation_id", substation_id));

		int size = lines.size();
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp(
				"draw", 1);
		map.put("recordsTotal", size);
		map.put("data", lines);
		map.put("recordsFiltered", size);
		return map;
	}

	
	/**
	 * 对线路进行删除，需要有line_admin:delete权限
	 */
	@RequestMapping("/del_line")
	@ResponseBody
	//@RequiresPermissions("line_admin:delete")
	public String deleLine(@RequestParam(required = true) String switchId,
			Model model, RedirectAttributes redirectAttributes) {

		String id = lineService.selectByPrimaryKey(switchId).getSubstationId();
		try {

			lineService.deleteByPrimaryKey(switchId);// 删除这个开关
		} catch (Exception e) {
			logger.error("删除开关失败！");
			return null;
		}
		return id;
	}

	/**
	 * 对线路进行修改或者添加，需要有line_admin:edit权限
	 */
	@RequestMapping("/edit_line")
	@ResponseBody
	//@RequiresPermissions("line_admin:edit")
	public String editSwitch(Line switch1, Model model,
			RedirectAttributes redirectAttributes) {

		// @RequestParam(required = true)
		// 进不来
		if (switch1.getId().equals("")) {
			switch1.setId(UUIDUtil.getUUID());
		}
		try {

			lineService.updateByPrimaryKey(switch1);
		} catch (Exception e) {

			logger.error("修改开关失败！");
			return null;
		}
		return switch1.getSubstationId();
	}

}
