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
import com.gdut.dongjun.service.device.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.device.ElectronicModuleService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/data_monitor/submodule/electronic")
public class ElectronicModuleController {

	@Autowired
	private ElectronicModuleService moduleService;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;
	@Autowired
	private ModuleHitchEventService moduleHitchEventService;
	@Autowired
	private HardwareServiceClient hardwareClient;
	
	/**
	 * TODO 要增加和DataMonitor的关联
	 * @param module
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit")
	@Transactional
	public ResponseMessage edit(ElectronicModule module, String monitorId) {
		if (null == module.getId() || "".equals(module.getId())) {
			
			List<ElectronicModule> modules = moduleService
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
			submodule.setModuleType(HitchConst.MODULE_ELECTRICITY);
			if (0 == submoduleService.updateByPrimaryKey(submodule)) {
				return ResponseMessage.warning("操作失败");
			}
		} else {
			ElectronicModule m = moduleService.selectByPrimaryKey(module.getId());
			if (null == m) {
				return ResponseMessage.warning("该设备未被注册");
			}
			List<ElectronicModule> modules = moduleService
					.selectByParameters(MyBatisMapUtil.warp("device_number", module.getDeviceNumber()));
			if (0 != modules.size() && !modules.get(0).getId().equals(module.getId())) {
				return ResponseMessage.warning("该设备地址已经被占用");
			}
			if (0 == moduleService.updateByPrimaryKeySelective(module)) {
				return ResponseMessage.warning("操作失败");
			}
			hardwareClient.getService().changeSubmoduleAddress(module.getId(), HitchConst.MODULE_ELECTRICITY);
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
		//删除子模块
		if (!moduleService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		//删除DataMonitorSubmodule
		List<DataMonitorSubmodule> submodules = submoduleService.selectByParameters(MyBatisMapUtil.warp("module_id", id));
		if (0 == submodules.size()) {
			return ResponseMessage.warning("操作失败");
		}
		DataMonitorSubmodule submodule = submodules.get(0);
		if (!submoduleService.deleteByPrimaryKey(submodule.getId())) {
			return ResponseMessage.warning("操作失败"); 
		}
		//删除报警信息
		moduleHitchEventService.deleteByParameters(MyBatisMapUtil.warp("module_id", id));
		return ResponseMessage.success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/list")
	public ResponseMessage list(String monitorId) {
		List<DataMonitorSubmodule> subList = submoduleService.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
		//TODO
		for (DataMonitorSubmodule sub : subList) {
			if (sub.getModuleType() == HitchConst.MODULE_ELECTRICITY) {
				ElectronicModule module = moduleService.selectByPrimaryKey(sub.getModuleId());
				return ResponseMessage.success(module);
			}
		}
		return ResponseMessage.warning(null);
	}
}
