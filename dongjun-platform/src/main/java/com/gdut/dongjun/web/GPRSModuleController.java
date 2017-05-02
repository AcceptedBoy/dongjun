package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.HitchConst;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DataMonitorSubmodule;
import com.gdut.dongjun.domain.po.GPRSModule;
import com.gdut.dongjun.dto.GPRSModuleDTO;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.RoleService;
import com.gdut.dongjun.service.device.DataMonitorSubmoduleService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/gprs")
public class GPRSModuleController {

	@Autowired
	private GPRSModuleService gprsService;
	@Autowired
	private PlatformGroupService pgService;
	@Autowired
	private UserService userService;
	@Autowired
	private HardwareServiceClient hardwareServiceClient;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DataMonitorSubmoduleService submoduleService;

	// 6f1b9f044e1346f299af9cc0fe7e005d
	// 6f1b9f044e1346f299af9cc0fe7e005d
	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(GPRSModule gprs, String monitorId) {
		if (null == gprs.getId()) {
			List<GPRSModule> modules = gprsService
					.selectByParameters(MyBatisMapUtil.warp("device_number", gprs.getDeviceNumber()));
			if (0 != modules.size()) {
				return ResponseMessage.warning("该设备地址已经被占用");
			}
			gprs.setId(UUIDUtil.getUUID());
			gprs.setAvailable(true);
			DataMonitorSubmodule submodule = new DataMonitorSubmodule();
			submodule.setId(UUIDUtil.getUUID());
			submodule.setAvailable(1);
			submodule.setDataMonitorId(monitorId);
			submodule.setModuleId(gprs.getId());
			submodule.setModuleType(HitchConst.MODULE_GPRS);
			if (0 == submoduleService.updateByPrimaryKey(submodule)) {
				return ResponseMessage.warning("操作失败");
			}
			if (0 == gprsService.updateByPrimaryKey(gprs)) {
				return ResponseMessage.warning("操作失败");
			} else {
				return ResponseMessage.success("操作成功");
			}
		} else {
			// GPRSModule module =
			// gprsService.selectByPrimaryKey(gprs.getDeviceNumber());
			List<GPRSModule> modules = gprsService
					.selectByParameters(MyBatisMapUtil.warp("device_number", gprs.getDeviceNumber()));
			if (0 != modules.size() && !modules.get(0).getId().equals(gprs.getId())) {
				return ResponseMessage.warning("该设备地址已经被占用");
			}
			if (0 == gprsService.updateByPrimaryKeySelective(gprs)) {
				return ResponseMessage.warning("操作失败");
			} else {
				return ResponseMessage.success("操作成功");
			}
		}
	}

	@ResponseBody
	@RequestMapping("/list")
	public ResponseMessage listGPRSModule(String monitorId, HttpSession session) {
		List<DataMonitorSubmodule> subList = submoduleService
				.selectByParameters(MyBatisMapUtil.warp("data_monitor_id", monitorId));
		// TODO 优化sql
		for (DataMonitorSubmodule sub : subList) {
			if (sub.getModuleType() == HitchConst.MODULE_GPRS) {
				GPRSModule module = gprsService.selectByPrimaryKey(sub.getModuleId());
				if (null == module) {
					return ResponseMessage.warning("操作失败");
				}
				return ResponseMessage.success(wrapIntoDTO(module));
			}
		}
		return ResponseMessage.warning(null);
	}

	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String id) {
		if (!gprsService.deleteByPrimaryKey(id)) {
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

//	@ResponseBody
//	@RequestMapping("/company_gprs")
//	public ResponseMessage getCompanyGPRS(HttpSession session) {
//		User user = userService.getCurrentUser(session);
//		PlatformGroup pg = pgService.selectByPrimaryKey(user.getCompanyId());
//		List<GPRSModule> list = gprsService.selectByParameters(MyBatisMapUtil.warp("group_id", pg.getId()));
//		return ResponseMessage.success(wrapIntoDTO(list));
//	}

	private GPRSModuleDTO wrapIntoDTO(GPRSModule gprs) {
		List<String> deviceNumbers = new ArrayList<String>();
		List<Integer> status = null;
		deviceNumbers.add(gprs.getDeviceNumber());
		status = hardwareServiceClient.getService().getGPRSModuleStatus(deviceNumbers);
		GPRSModuleDTO dto = new GPRSModuleDTO(gprs, status.get(0));
		return dto;
	}

	private List<GPRSModuleDTO> wrapIntoDTO(List<GPRSModule> list) {
		List<GPRSModuleDTO> dtos = new ArrayList<GPRSModuleDTO>();
		List<String> deviceNumbers = new ArrayList<String>();
		List<Integer> status = null;
		for (GPRSModule gprs : list) {
			deviceNumbers.add(gprs.getDeviceNumber());
		}

		status = hardwareServiceClient.getService().getGPRSModuleStatus(deviceNumbers);

		int i = 0;
		for (GPRSModule gprs : list) {
			GPRSModuleDTO dto = new GPRSModuleDTO(gprs, status.get(i));
			dtos.add(dto);
			i++;
		}
		return dtos;
	}

}
