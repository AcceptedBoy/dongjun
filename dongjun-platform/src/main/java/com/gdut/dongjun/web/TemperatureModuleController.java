package com.gdut.dongjun.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.HitchConst;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DeviceGroupMapping;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.TemperatureSensorService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@RequestMapping("/dongjun/data_monitor/submodule/temperature")
@Controller
public class TemperatureModuleController {

	@Autowired
	private TemperatureModuleService moduleService;
	@Autowired
	private UserService userService;
	@Autowired
	private HardwareServiceClient hardwareClient;
	@Autowired
	private ModuleHitchEventService moduleHitchEventService;
	@Autowired
	private TemperatureSensorService sensorService;
	@Autowired
	private DeviceGroupMappingService dgmService;
	
	/**
	 * TODO 要增加和DataMonitor的关联
	 * @param module
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit")
	@Transactional
	public ResponseMessage edit(TemperatureModule module, 
			@RequestParam(value="deviceGroupId") String deviceGroupId,
			HttpSession session) {
		if (null == module.getId() || "".equals(module.getId())) {
//			Subject subject = SecurityUtils.getSubject();
//			if (!subject.hasRole("super_admin")) {
//				User user = userService.getCurrentUser(session);
//				module.setGroupId(user.getCompanyId());
//			}
			//	没有传companyId回来，说明是公司管理人操作，否则是超管操作
			if (null == module.getCompanyId() || "".equals(module.getCompanyId())) {
				module.setCompanyId(userService.getCurrentUser(session).getCompanyId());
			}
			List<TemperatureModule> modules = moduleService
					.selectByParametersNoDel(MyBatisMapUtil.warp("device_number", module.getDeviceNumber()));
			if (0 != modules.size()) {
				return ResponseMessage.warning("该设备地址已经被占用");
			}
			module.setId(UUIDUtil.getUUID());
			if (0 == moduleService.updateByPrimaryKey(module)) {
				return ResponseMessage.warning("操作失败");
			}
			DeviceGroupMapping m = new DeviceGroupMapping();
			m.setId(UUIDUtil.getUUID());
			m.setDeviceGroupId(deviceGroupId);
			m.setModuleId(module.getId());
			m.setType(3);
			dgmService.insert(m);
		} else {
			TemperatureModule m = moduleService.selectByPrimaryKeyNoDel(module.getId());
			if (null == m) {
				return ResponseMessage.warning("该设备未被注册"); 
			}
			List<TemperatureModule> modules = moduleService
					.selectByParametersNoDel(MyBatisMapUtil.warp("device_number", module.getDeviceNumber()));
			if (0 != modules.size() && !modules.get(0).getId().equals(module.getId())) {
				return ResponseMessage.warning("该设备地址已经被占用");
			}
			if (0 == moduleService.updateByPrimaryKeySelective(module)) {
				return ResponseMessage.warning("操作失败");
			}
			//更新硬件系统的上下限缓存
			hardwareClient.getService().changeTemperatureDevice(module.getId());
			hardwareClient.getService().changeSubmoduleAddress(module.getId(), HitchConst.MODULE_TEMPERATURE);
		}
		return ResponseMessage.success("操作成功");
	}
	
	/**
	 * TODO 删除和DataMonitor的关联，加事务，抛异常
	 * @param id
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping("/del")
//	public ResponseMessage del(String id) {
//		//删除子模块
//		if (!moduleService.deleteByPrimaryKey(id)) {
//			return ResponseMessage.warning("操作失败");
//		}
//		//删除DataMonitorSubmodule
//		List<DataMonitorSubmodule> submodules = submoduleService.selectByParameters(MyBatisMapUtil.warp("module_id", id));
//		if (0 == submodules.size()) {
//			return ResponseMessage.warning("操作失败");
//		}
//		DataMonitorSubmodule submodule = submodules.get(0);
//		if (!submoduleService.deleteByPrimaryKey(submodule.getId())) {
//			return ResponseMessage.warning("操作失败"); 
//		}
//		//删除报警信息
//		moduleHitchEventService.deleteByParameters(MyBatisMapUtil.warp("module_id", id));
//		//删除传感器数据
//		sensorService.deleteByParameters(MyBatisMapUtil.warp("device_id", id));
//		return ResponseMessage.success("操作成功");
//	}
	
	@ResponseBody
	@RequestMapping("/list")
	public ResponseMessage list(@RequestParam(value="moduleId") String moduleId) {
//		List<DataMonitorSubmodule> subList = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		//TODO
//		for (DataMonitorSubmodule sub : subList) {
//			if (sub.getModuleType() == 3) {
//				List<TemperatureModule> module = moduleService.selectByParameters(MyBatisMapUtil.warp("id", sub.getModuleId()));
//				if (0 == module.size()) {
//					return ResponseMessage.warning(null);
//				}
//				if (null == module || module.size() > 1) {
//					return ResponseMessage.warning("操作失败");
//				}
//				return ResponseMessage.success(module.get(0));
//			}
//		}
//		return ResponseMessage.warning(null);
		TemperatureModule module = moduleService.selectByPrimaryKey(moduleId);
		if (null != module) {
			return ResponseMessage.success(module);
		}
		return ResponseMessage.warning(null);
	}

//	@ResponseBody
//	@RequestMapping("/download_excel")
//	public ResponseMessage downloadExcel(String monitorId) {
//		List<DataMonitorSubmodule> subList = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
//		//TODO
//		for (DataMonitorSubmodule sub : subList) {
//			if (sub.getModuleType() == 3) {
//				List<TemperatureModule> module = moduleService.selectByParameters(MyBatisMapUtil.warp("id", sub.getModuleId()));
//				if (0 == module.size()) {
//					return ResponseMessage.warning(null);
//				}
//				if (null == module || module.size() > 1) {
//					return ResponseMessage.warning("操作失败");
//				}
//				return ResponseMessage.success(module.get(0));
//			}
//		}
//		return ResponseMessage.warning(null);
//	}
	
}
