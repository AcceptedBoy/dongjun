package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.UserLog;
import com.gdut.dongjun.dto.UserLogDTO;
import com.gdut.dongjun.service.UserLogService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.util.MapUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;

/**
 * @author Gordan_Deng
 * @date 2017年4月11日
 */
@Controller
@RequestMapping("/dongjun/log")
public class LogController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserLogService userLogService;
	
	@RequiresAuthentication
	@RequestMapping("/get_user_log")
	@ResponseBody
	public ResponseMessage getLog(HttpSession session) {
		User currentUser = userService.getCurrentUser(session);
		List<UserLog> logs = userLogService.selectByParameters(MyBatisMapUtil.warp("company_id", currentUser.getCompanyId()));
		HashMap<String, Object> map = (HashMap<String, Object>) MapUtil.warp("draw", 1);
		map.put("recordsTotal", logs.size());
		map.put("data", wrapUL(logs));
		map.put("recordsFiltered", logs.size());
		return ResponseMessage.success(map);
	}
	
	private List<UserLogDTO> wrapUL(List<UserLog> logs) {
		List<UserLogDTO> dtos = new ArrayList<UserLogDTO>();
		for (UserLog log : logs) {
			UserLogDTO dto = new UserLogDTO();
			dto.setId(log.getId());
			dto.setDate(log.getDate());
			dto.setType(log.getType());
			dto.setUserId(log.getUserId());
			dto.setCompanyId(log.getCompanyId());
			dto.setName(userService.selectByPrimaryKey(log.getUserId()).getRealName());
			dtos.add(dto);
		}
		return dtos;
	}
	
	
}
