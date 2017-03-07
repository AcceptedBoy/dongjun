package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.UserDeviceMapping;
import com.gdut.dongjun.service.UserDeviceMappingService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/user_device_mapping")
public class UserDeviceMappingController {

	@Autowired
	private UserDeviceMappingService mappingService;
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/edit")
	public ResponseMessage edit(UserDeviceMapping mapping) {
		if (null == mapping.getId()) {
			mapping.setId(UUIDUtil.getUUID());
		}
		if (0 == mappingService.updateByPrimaryKey(mapping)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/del")
	public ResponseMessage del(String id) {
		if (!mappingService.deleteByPrimaryKey(id)) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@ResponseBody
	@RequestMapping("/get_user_mapping")
	public ResponseMessage getUserMapping(HttpSession session) {
		String userId = userService.getCurrentUser(session).getId();
		List<UserDeviceMapping> list = mappingService.selectByParameters(MyBatisMapUtil.warp("user_id", userId));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		map.put("recordsTotal", list.size());
		map.put("data", list);
		map.put("recordsFiltered", list.size());
		return ResponseMessage.success(map);
	}
	
}
