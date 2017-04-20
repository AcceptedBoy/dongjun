package com.gdut.dongjun.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.dto.TemperatureMeasureHitchEventDTO;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.service.device.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@RequestMapping("/dongjun/data_monitor/submodule/temperature")
@Controller
public class TemperatureModuleController {

	@Autowired
	private TemperatureModuleService moduleService;
	@Autowired
	private TemperatureMeasureHitchEventService eventService;
	@Autowired
	private ModuleHitchEventService moduleHitchService;
	@Autowired
	private UserService userService;
	@Autowired
	private DataMonitorService monitorService;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;
	
	/**
	 * TODO 要增加和DataMonitor的关联
	 * @param module
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(TemperatureModule module) {
		if (null == module.getId() || "".equals(module.getId())) {
			module.setId(UUIDUtil.getUUID());
		}
		if (0 == moduleService.updateByPrimaryKey(module)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	/**
	 * TODO 删除和DataMonitor的关联
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String id) {
		if (!moduleService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/list")
	public ResponseMessage list(String monitorId) {
		List<DataMonitorSubmodule> subList = submoduleService.selectByParameters(MyBatisMapUtil.warp("monitor_id", monitorId));
		String id = null;
		for (DataMonitorSubmodule sub : subList) {
			if (sub.getModuleType() == 3) {
				List<TemperatureModule> module = moduleService.selectByParameters(MyBatisMapUtil.warp("id", sub.getModuleId()));
				if (0 == module.size()) {
					return ResponseMessage.warning(null);
				}
				if (null == module || module.size() > 1) {
					return ResponseMessage.warning("操作失败");
				}
				return ResponseMessage.success(module.get(0));
			}
		}
		return ResponseMessage.warning(null);
	}
	
	@ResponseBody
	@RequestMapping("/hitch/module")
	public ResponseMessage moduleHitch(String id) {
		return ResponseMessage.success(moduleHitchService.selectByParameters(MyBatisMapUtil.warp("module_id", id)));
	}
	
	@ResponseBody
	@RequestMapping("/hitch/measure")
	public ResponseMessage measureHitch(HttpSession session) {
		User user = userService.getCurrentUser(session);
		List<TemperatureMeasureHitchEventDTO> eventList = eventService.selectMeasureHitch(user.getCompanyId());
		return ResponseMessage.success(eventList);
	}
	
	
}
