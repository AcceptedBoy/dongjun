package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.OperationLog;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.UserLog;
import com.gdut.dongjun.service.OperationLogService;
import com.gdut.dongjun.service.UserLogService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;

@Controller
@RequestMapping("/dongjun/log")
public class LogController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserLogService userLogService;
	
	@Autowired
	private OperationLogService operationLogService;
	
	@RequiresAuthentication
	@RequestMapping("/get_user_log")
	@ResponseBody
	public ResponseMessage getLog(HttpSession session) {
		User currentUser = userService.getCurrentUser(session);
		List<UserLog> logs = userLogService.selectByParameters(MyBatisMapUtil.warp("company_id", currentUser.getCompanyId()));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		map.put("recordsTotal", logs.size());
		map.put("data", logs);
		map.put("recordsFiltered", logs.size());
		return ResponseMessage.success(map);
	}
	
	@RequiresAuthentication
	@RequestMapping("/get_operation_log")
	@ResponseBody
	public ResponseMessage getOperationLog(HttpSession session) {
		User currentUser = userService.getCurrentUser(session);
		List<OperationLog> logs = operationLogService.selectByParameters(MyBatisMapUtil.warp("company_id", currentUser.getCompanyId()));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		map.put("recordsTotal", logs.size());
		map.put("data", logs);
		map.put("recordsFiltered", logs.size());
		return ResponseMessage.success(map);
	}
	
}
