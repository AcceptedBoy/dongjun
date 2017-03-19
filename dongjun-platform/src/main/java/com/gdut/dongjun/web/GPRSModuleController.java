package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.GPRSModule;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.dto.GPRSModuleDTO;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserService;
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
	
	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(GPRSModule gprs) {
		if (null == gprs.getId()) {
			gprs.setId(UUIDUtil.getUUID());
			gprs.setAvailable(true);
			if (0 == 	gprsService.updateByPrimaryKey(gprs)) {
				return ResponseMessage.warning("操作失败");
			} else {
				return ResponseMessage.success("操作成功");
			}
		} else {
			if (0 == 	gprsService.updateByPrimaryKeySelective(gprs)) {
				return ResponseMessage.warning("操作失败");
			} else {
				return ResponseMessage.success("操作成功");
			}
		}
	}
	
	@ResponseBody
	@RequestMapping("/list")
	public ResponseMessage lisGPRSModule(String platformId) {
		List<GPRSModule> modules = gprsService.selectByParameters(MyBatisMapUtil.warp("group_id", platformId));
		return ResponseMessage.success(wrapIntoDTO(modules));
	}
	
	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String id) {
		if (!gprsService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/company_gprs")
	public ResponseMessage getCompanyGPRS(HttpSession session) {
		User user = userService.getCurrentUser(session);
		PlatformGroup pg = pgService.selectByPrimaryKey(user.getCompanyId());
		List<GPRSModule> list = gprsService.selectByParameters(MyBatisMapUtil.warp("group_id", pg.getId()));
		return ResponseMessage.success(wrapIntoDTO(list));
	}
	
	private List<GPRSModuleDTO> wrapIntoDTO(List<GPRSModule> list) {
		List<GPRSModuleDTO> dtos = new ArrayList<GPRSModuleDTO>();
		List<String> ids = new ArrayList<String>();
		List<Integer> status = null;
		for (GPRSModule gprs : list) {
			ids.add(gprs.getId());
		}
		
			status = hardwareServiceClient.getService().getGPRSModuleStatus(ids);

		int i = 0;
		for (GPRSModule gprs : list) {
			GPRSModuleDTO dto = new GPRSModuleDTO(gprs, status.get(i));
			dtos.add(dto);
			i++;
		}
		return dtos;
	}
	
	@RequestMapping("/addgprs")
	@ResponseBody
	public String add() {
		GPRSModule module = new GPRSModule();
		module.setAddress("156");
		module.setAddress("1");
		module.setDeviceNumber("156");
		module.setGmtCreate(new Date());
		module.setGmtModified(new Date());
		module.setGroupId("1");
		module.setId("testid");
		module.setName("156Name");
		gprsService.insert(module);
		return "add!!";
	}
	
}
