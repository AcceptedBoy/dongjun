package com.gdut.dongjun.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.web.vo.ResponseMessage;

@Controller
@RequestMapping("/dongjun/business")
public class BusinessController {
	
	@ResponseBody
	@RequestMapping("/line/list")
	public ResponseMessage listLine(String companyId) {
		
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/device/list")
	public ResponseMessage listDevice(String lineId) {
		
		return null;
	}
	
	/**
	 * 批量更改设备终止日期
	 * @param deviceId
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/device/edit_date")
	public ResponseMessage editDevice(String[] deviceId, String endDate) {
		
		return null;
	}
	
	/**
	 * 批量处理设备可用性
	 * @param deviceId
	 * @param available
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/device/available")
	public ResponseMessage deviceEdit(String[] deviceId, String available) {
		
		return null;
	}

}
