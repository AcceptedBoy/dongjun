package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.TemperatureMeasureHitchEvent;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Controller
@RequestMapping("/dongjun/measure_event")
public class MeasureHitchEventController {
	
	@Autowired
	private TemperatureMeasureHitchEventService temEventService;
	@Autowired
	private UserService userService;

	@RequiresAuthentication
	@RequestMapping("/temperature_event")
	@ResponseBody
	public ResponseMessage getTemEvent(String companyId, HttpSession session) {
		if (null == companyId || "".equals(companyId)) {
			companyId = userService.getCurrentUser(session).getCompanyId();
		}
		List<TemperatureMeasureHitchEvent> events = temEventService.selectByParameters(MyBatisMapUtil.warp("company_id", companyId));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		map.put("recordsTotal", events.size());
		map.put("recordsFiltered", events.size());
		map.put("data", events);
		return ResponseMessage.success(events);
	}
	
	
}
