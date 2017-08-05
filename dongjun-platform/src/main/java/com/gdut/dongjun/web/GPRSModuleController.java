package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.ElectronicModule;
import com.gdut.dongjun.domain.po.GPRSModule;
import com.gdut.dongjun.domain.po.TemperatureModule;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.ElectronicModuleService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.webservice.client.HardwareServiceClient;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;
import com.gdut.dongjun.web.vo.GPRSModuleVO;

@Controller
@RequestMapping("/dongjun/module/gprs")
public class GPRSModuleController {

	@Autowired
	private GPRSModuleService gprsService;
	@Autowired
	private UserService userService;
	@Autowired
	private HardwareServiceClient hardwareServiceClient;
	@Autowired
	private ModuleHitchEventService moduleHitchEventService;
	@Autowired
	private ElectronicModuleService elecService;
	@Autowired
	private TemperatureModuleService temService;

	// 6f1b9f044e1346f299af9cc0fe7e005d
	// 6f1b9f044e1346f299af9cc0fe7e005d
	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(GPRSModule gprs, HttpSession session) {
		if (null == gprs.getId()) {

			//	若没有companyId，即公司管理人操作，否则是超管操作
			if (null == gprs.getCompanyId() || "".equals(gprs.getCompanyId())) {
				gprs.setCompanyId(userService.getCurrentUser(session).getCompanyId());
			}
			
			List<GPRSModule> modules = gprsService
					.selectByParametersNoDel(MyBatisMapUtil.warp("device_number", gprs.getDeviceNumber()));
			if (0 != modules.size()) {
				return ResponseMessage.warning("该设备地址已经被占用");
			}
			gprs.setId(UUIDUtil.getUUID());
			gprs.setAvailable(true);
			//设置DataMonitorSubmodule
//			DataMonitorSubmodule submodule = new DataMonitorSubmodule();
//			submodule.setId(UUIDUtil.getUUID());
//			submodule.setAvailable(1);
//			submodule.setDataMonitorId(monitorId);
//			submodule.setModuleId(gprs.getId());
//			submodule.setModuleType(HitchConst.MODULE_GPRS);
//			if (0 == submoduleService.updateByPrimaryKey(submodule)) {
//				return ResponseMessage.warning("操作失败");
//			}
			if (0 == gprsService.updateByPrimaryKey(gprs)) {
				return ResponseMessage.warning("操作失败");
			} else {
				return ResponseMessage.success("操作成功");
			}
		} else {
			GPRSModule m = gprsService.selectByPrimaryKey(gprs.getId());
			if (null == m) {
				return ResponseMessage.warning("该设备未注册"); 
			}
			List<GPRSModule> modules = gprsService
					.selectByParametersNoDel(MyBatisMapUtil.warp("device_number", gprs.getDeviceNumber()));
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
	public ResponseMessage listGPRSModule(String companyId, HttpSession session) {
		if (null == companyId) {
			companyId = userService.getCurrentUser(session).getCompanyId();
		}
		List<GPRSModule> modules = gprsService.selectByParametersNoDel(MyBatisMapUtil.warp("company_id", companyId));
		if (0 != modules.size()) {
			return ResponseMessage.success(wrapIntoVO(modules));
		}
		return ResponseMessage.warning("该子模块没有数据");
	}

	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(@RequestParam(value = "id") String id) {
		//删除GPRSModule
		List<ElectronicModule> elecList =
				elecService.selectByParametersNoDel(MyBatisMapUtil.warp("gprs_id", id));
		List<TemperatureModule> temList = 
				temService.selectByParametersNoDel(MyBatisMapUtil.warp("gprs_id", id));
		if ((null == elecList || 0 == elecList.size()) && 
				(null == temList || 0 == temList.size())) {
			if (gprsService.deleteByPrimaryKey(id)) {
				return ResponseMessage.success("操作成功");
			} else {
				return ResponseMessage.danger("操作失败");
			}
		}
		return ResponseMessage.warning("删除失败，仍有设备引用该GPRS，请再次检查");
		
//		//删除报警信息
//		moduleHitchEventService.deleteByParameters(MyBatisMapUtil.warp("module_id", id));
	}

//	@ResponseBody
//	@RequestMapping("/company_gprs")
//	public ResponseMessage getCompanyGPRS(HttpSession session) {
//		User user = userService.getCurrentUser(session);
//		PlatformGroup pg = pgService.selectByPrimaryKey(user.getCompanyId());
//		List<GPRSModule> list = gprsService.selectByParameters(MyBatisMapUtil.warp("group_id", pg.getId()));
//		return ResponseMessage.success(wrapIntoDTO(list));
//	}

	private GPRSModuleVO wrapIntoVO(GPRSModule gprs) {
		List<String> deviceNumbers = new ArrayList<String>();
		List<Integer> status = null;
		deviceNumbers.add(gprs.getDeviceNumber());
		status = hardwareServiceClient.getService().getGPRSModuleStatus(deviceNumbers);
		GPRSModuleVO dto = new GPRSModuleVO(gprs, status.get(0));
		return dto;
	}

	private List<GPRSModuleVO> wrapIntoVO(List<GPRSModule> list) {
		List<GPRSModuleVO> dtos = new ArrayList<GPRSModuleVO>();
		List<String> deviceNumbers = new ArrayList<String>();
		List<Integer> status = null;
		for (GPRSModule gprs : list) {
			deviceNumbers.add(gprs.getDeviceNumber());
		}

		status = hardwareServiceClient.getService().getGPRSModuleStatus(deviceNumbers);

		int i = 0;
		for (GPRSModule gprs : list) {
			GPRSModuleVO dto = new GPRSModuleVO(gprs, status.get(i));
			dtos.add(dto);
			i++;
		}
		return dtos;
	}

//	public static void main(String[] args) {
//		String a = "685d5d68f36800031c030168001b00021b00021800020100022400020100011c00021b00021800020100022400020100011c00022f00021b00021800020100022400020100011c00021b00021900021b00021800020100021c00022700020100016f16";
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < a.length(); i=2+i) {
//			sb.append(a.substring(i, i + 2));
//			sb.append(" ");
//		}
//		System.out.println(sb.toString());
//	}
}
