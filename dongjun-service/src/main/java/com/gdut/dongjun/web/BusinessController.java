package com.gdut.dongjun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.po.HighVoltageSwitch;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.web.vo.ResponseMessage;

@Controller
@RequestMapping("/dongjun")
public class BusinessController {
	
	@Autowired
	private HighVoltageSwitchService switchService;
	
	/**
	 * 更改设备终止日期
	 * @param deviceId
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/switch/edit_date")
	public ResponseMessage switchAvailable(String switchId, String endDate) {
		HighVoltageSwitch s = switchService.selectByPrimaryKey(switchId);
		s.setExpireTime(TimeUtil.timeParse(endDate));
		s.setAvailable(true);
		if (0 != switchService.updateByPrimaryKey(s)) {
			return ResponseMessage.success("success");
		}
		return ResponseMessage.warning("fail");
	}
	
	/**
	 * 处理设备可用性
	 * @param deviceId
	 * @param available
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/switch/disabled")
	public ResponseMessage switchDisabled(String switchId) {
		HighVoltageSwitch s = switchService.selectByPrimaryKey(switchId);
		s.setAvailable(false);
		if (0 != switchService.updateByPrimaryKey(s)) {
			return ResponseMessage.success("success");
		}
		return ResponseMessage.warning("fail");
	}

}
