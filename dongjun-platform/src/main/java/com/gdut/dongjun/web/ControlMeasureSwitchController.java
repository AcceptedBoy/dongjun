package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.ControlMearsureSwitch;
import com.gdut.dongjun.service.ControlMearsureSwitchService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun")
public class ControlMeasureSwitchController {

	@Autowired
	private ControlMearsureSwitchService switchService;

	private static final Logger logger = Logger
			.getLogger(ControlMeasureHitchEventController.class);

	/**
	 * 
	 * @Title: getLineSwitchList
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return String
	 * @throws high_voltage_
	 */
	@RequestMapping("/control_measure_switch_manager")
	public String getLineSwitchList(String platformId, Model model) {

		if (platformId != null) {

			model.addAttribute("switches", switchService
					.selectByParameters(MyBatisMapUtil.warp("group_id", platformId)));
		} else {
			model.addAttribute("switches",
					switchService.selectByParameters(null));
		}
		return "control_measure_switch_manager";
	}

	/**
	 * 
	 * @Title: getAllLowVoltage_Switch
	 * @Description: TODO
	 * @param @param model
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/get_all_control_measure_switch")
	@ResponseBody
	public Object getAllLowVoltage_Switch(Model model) {

		return switchService.selectByParameters(null);
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
	@RequestMapping("/control_measure_switch_list_by_platform_id")
	@ResponseBody
	public Object getLineSwitchListByLineId(String platformId, Model model) {
		if (null == platformId && "".equals(platformId)) {
			return "";
		}

		List<ControlMearsureSwitch> switchs = switchService
				.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp(
				"draw", 1);
		int size = switchs.size();
		map.put("recordsTotal", size);
		map.put("data", switchs);
		map.put("recordsFiltered", size);
		return map;
	}

	/**
	 * 
	 * @Title: selectCMByLineIdInAsc
	 * @Description: TODO
	 * @param @param lineId
	 * @param @param model
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("/selectCMByPlatformIdInAsc")
	@ResponseBody
	public Object selectCMByLineIdInAsc(
			@RequestParam(required = true) String platformId, Model model) {

		List<ControlMearsureSwitch> switchs = switchService
				.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));

		return switchs;
	}

	/**
	 * 
	 * @Title: delSwitch
	 * @Description: TODO
	 * @param @param switchId
	 * @param @param model
	 * @param @param redirectAttributes
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/del_control_measure_switch")
	@ResponseBody
	public ResponseMessage delSwitch(@RequestParam(required = true) String switchId,
			Model model, RedirectAttributes redirectAttributes) {

		int platformId = switchService.selectByPrimaryKey(switchId).getGroupId();
		try {

			switchService.deleteByPrimaryKey(switchId);// 删除这个开关
		} catch (Exception e) {
			logger.info("删除管控开关失败");
			e.printStackTrace();
			return ResponseMessage.danger("删除管控开关失败");
		}
		return ResponseMessage.success(platformId);
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
	@RequestMapping("/edit_control_measure_switch")
	@ResponseBody
	public ResponseMessage editSwitch(
			ControlMearsureSwitch switch1, 
			Model model,
			String platformId,
			RedirectAttributes redirectAttributes) {

		// @RequestParam(required = true)
		// 进不来
		if (switch1.getId() == null || "".equals(switch1.getId())) {
			switch1.setId(UUIDUtil.getUUID());
		}
		switch1.setGroupId(Integer.parseInt(platformId));
		try {
			switchService.updateByPrimaryKeySelective(switch1);
		} catch (Exception e) {
			logger.info("修改管控开关失败");
			e.printStackTrace();
			return ResponseMessage.danger("修改管控开关失败");
		}
		return ResponseMessage.success(switch1.getGroupId());
	}

}
