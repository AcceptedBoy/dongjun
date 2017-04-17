package com.gdut.dongjun.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@RequestMapping("/dongjun/data_monitor/submodule/temperature")
@Controller
public class TemperatureModuleController {

	@Autowired
	private TemperatureModuleService moduleService;
	
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
		List<TemperatureModule> module = moduleService.selectByParameters(MyBatisMapUtil.warp("group_id", monitorId));
		if (null == module || module.size() > 1) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success(module.get(0));
	}
	
	
}
