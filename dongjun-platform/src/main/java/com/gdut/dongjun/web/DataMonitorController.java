package com.gdut.dongjun.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/data_monitor")
public class DataMonitorController {

	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(DataMonitor monitor) {
		if (null == monitor.getId() || "".equals(monitor.getId())) {
			monitor.setId(UUIDUtil.getUUID());
		}
		return null;
	}
}
