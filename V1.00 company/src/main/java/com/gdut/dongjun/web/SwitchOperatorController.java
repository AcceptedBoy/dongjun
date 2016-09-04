package com.gdut.dongjun.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdut.dongjun.domain.po.HighSwitchUser;
import com.gdut.dongjun.domain.po.enums.Protocol;
import com.gdut.dongjun.service.HighSwitchUserService;
import com.gdut.dongjun.util.JsonUtil;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@RestController
@RequestMapping("/dongjun/operator")
public class SwitchOperatorController {

	@Autowired
	private HighSwitchUserService highSwitchOperatorService;
	
	/**
	 * 当传入的protocol：0 低压；1高压；2管控；
	 * @param session
	 * @param type
	 * @return
	 */
	@RequestMapping("/get_manager_switch")
	public Object getSwitchByUser(HttpSession session, 
			@RequestParam(required=false) Integer protocol, 
			@RequestParam(required=false) String userId) {
		
		switch(protocol) {
			case 1 : 
				return highSwitchOperatorService.getSwitchByUserId(userId);
			default: return null;
		}
	}
	
	@RequestMapping("/add_manager_switch")
	public Object addManagerSwitch(
			@RequestParam(required=false) String userId,
			@RequestParam(required=false) Integer protocol,
			@RequestParam(required=false) String switchId) {
		
		if(!Protocol.hasContain(protocol)) {
			return JsonUtil.ERROR;
		}
		
		switch(protocol) {
			case 1:
				HighSwitchUser switchUser = new HighSwitchUser();
				switchUser.setId(UUIDUtil.getUUID());
				switchUser.setUserId(userId);
				switchUser.setHighVoltageSwitchId(switchId);
				if(highSwitchOperatorService.insert(switchUser) == 1) {
					return JsonUtil.SUCCESS;
				}
				break;
			default:break;
		}
		
		return JsonUtil.ERROR;
	}
	
	@RequestMapping("delete_manager_switch")
	public Object deleteManagerSwitch(
			@RequestParam(required=false) String userId,
			@RequestParam(required=false) Integer protocol,
			@RequestParam(required=false) String switchId) {
		
		if(!Protocol.hasContain(protocol)) {
			return JsonUtil.ERROR;
		}
		Map<String, Object> map = new HashMap<>(2);
		map.put("user_id", userId);
		map.put("high_voltage_switch_id", switchId);
		switch(protocol) {
			case 1:
				if(highSwitchOperatorService.deleteByParameters(MyBatisMapUtil.warp(map))) {
					return JsonUtil.SUCCESS;
				}
				break;
			default:break;
		}
		return JsonUtil.ERROR;
	}
}
