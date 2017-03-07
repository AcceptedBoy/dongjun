package com.gdut.dongjun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.GPRSModule;
import com.gdut.dongjun.service.GPRSModuleService;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/gprs")
public class GPRSModuleController {

	@Autowired
	private GPRSModuleService gprsService;
	
	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(GPRSModule gprs) {
		if (null != gprs.getId()) {
			gprs.setId(UUIDUtil.getUUID());
		}
		if (0 == 	gprsService.updateByPrimaryKey(gprs)) {
			return ResponseMessage.success("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String id) {
		if (!gprsService.deleteByPrimaryKey(id)) {
			return ResponseMessage.success("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
}
