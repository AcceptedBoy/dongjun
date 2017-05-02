package com.gdut.dongjun.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.HitchConst;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.device.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@RequestMapping("/dongjun/data_monitor/submodule/temperature")
@Controller
public class TemperatureModuleController {

	@Autowired
	private TemperatureModuleService moduleService;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;
	
	/**
	 * TODO 要增加和DataMonitor的关联
	 * @param module
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit")
	@Transactional
	public ResponseMessage edit(TemperatureModule module, String monitorId) {
		if (null == module.getId() || "".equals(module.getId())) {
			
			List<TemperatureModule> modules = moduleService
					.selectByParameters(MyBatisMapUtil.warp("device_number", module.getDeviceNumber()));
			if (0 != modules.size()) {
				return ResponseMessage.warning("该设备地址已经被占用");
			}
			module.setId(UUIDUtil.getUUID());
			if (0 == moduleService.updateByPrimaryKey(module)) {
				return ResponseMessage.warning("操作失败");
			}
			DataMonitorSubmodule submodule = new DataMonitorSubmodule();
			submodule.setId(UUIDUtil.getUUID());
			submodule.setAvailable(1);
			submodule.setDataMonitorId(monitorId);
			submodule.setModuleId(module.getId());
			submodule.setModuleType(HitchConst.MODULE_TEMPERATURE);
			if (0 == submoduleService.updateByPrimaryKey(submodule)) {
				return ResponseMessage.warning("操作失败");
			}
		} else {
			List<TemperatureModule> modules = moduleService
					.selectByParameters(MyBatisMapUtil.warp("device_number", module.getDeviceNumber()));
			if (0 != modules.size() && !modules.get(0).getId().equals(module.getId())) {
				return ResponseMessage.warning("该设备地址已经被占用");
			}
			if (0 == moduleService.updateByPrimaryKeySelective(module)) {
				return ResponseMessage.warning("操作失败");
			}
		}

		
		return ResponseMessage.success("操作成功");
	}
	
	/**
	 * TODO 删除和DataMonitor的关联，加事务，抛异常
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String id) {
		if (!moduleService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		List<DataMonitorSubmodule> submodules = submoduleService.selectByParameters(MyBatisMapUtil.warp("module_id", id));
		if (0 == submodules.size()) {
			return ResponseMessage.warning("操作失败");
		}
		DataMonitorSubmodule submodule = submodules.get(0);
		if (!submoduleService.deleteByPrimaryKey(submodule.getId())) {
			return ResponseMessage.warning("操作失败"); 
		}
		return ResponseMessage.success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/list")
	public ResponseMessage list(String monitorId) {
		List<DataMonitorSubmodule> subList = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
		//TODO
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
	
	
}
