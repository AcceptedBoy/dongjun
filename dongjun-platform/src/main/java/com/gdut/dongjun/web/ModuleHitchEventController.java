package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.ModuleHitchEvent;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.device.TemperatureModuleService;
import com.gdut.dongjun.service.device.event.ModuleHitchEventService;
import com.gdut.dongjun.service.device.event.TemperatureMeasureHitchEventService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.TimeUtil;
import com.gdut.dongjun.web.vo.ModuleHitchEventVO;
import com.gdut.dongjun.web.vo.TemperatureMeasureHitchEventVO;

/**
 * 这地方查询数据非常非常慢，后面再加入分页
 * @author Gordan_Deng
 * @date 2017年4月21日
 */
@Controller
@RequestMapping("/dongjun/hitch")
public class ModuleHitchEventController {

	@Autowired
	private ModuleHitchEventService moduleHitchService;
	@Autowired
	private TemperatureModuleService temModuleService;
	@Autowired
	private UserService userService;
	@Autowired
	private TemperatureMeasureHitchEventService measureEventService;
	
	@ResponseBody
	@RequestMapping("/module")
	public ResponseMessage moduleHitch(String companyId, String moduleId, HttpSession session) {
		if (null == moduleId || "".equals(moduleId)) {
			if (null == companyId || "".equals(companyId)) {
				companyId = userService.getCurrentUser(session).getCompanyId();
			}
			List<ModuleHitchEvent> list = moduleHitchService.selectByParameters(MyBatisMapUtil.warp("company_id", companyId));
			List<ModuleHitchEventVO> dtoList = new ArrayList<ModuleHitchEventVO>();
			for (ModuleHitchEvent e : list) {
				dtoList.add(wrap(e));
			}
			return ResponseMessage.success(dtoList);
		} else {
			List<ModuleHitchEvent> list = moduleHitchService.selectByParameters(MyBatisMapUtil.warp("module_id", moduleId));
			List<ModuleHitchEventVO> dtoList = new ArrayList<ModuleHitchEventVO>();
			for (ModuleHitchEvent e : list) {
				dtoList.add(wrap(e));
			}
			return ResponseMessage.success(dtoList);
		}
	}
	
	private ModuleHitchEventVO wrap(ModuleHitchEvent event) {
		ModuleHitchEventVO dto = new ModuleHitchEventVO();
		dto.setId(event.getId());
		dto.setName(getModuleName(event.getType(), event.getModuleId()));
		dto.setGmtCreate(event.getGmtCreate());
		dto.setGmtModified(event.getGmtModified());
		dto.setCompanyId(event.getCompanyId());
		dto.setHitchReason(event.getHitchReason());
		dto.setHitchTime(TimeUtil.timeFormat(event.getHitchTime()));
		dto.setModuleId(event.getModuleId());
		dto.setType(event.getType());
		return dto;
	}
	
	private String getModuleName(int type, String moduleId) {
		switch (type / 100) {
		case 3 : return temModuleService.selectByPrimaryKey(moduleId).getName();
		default : return null;
		}
	}

	@ResponseBody
	@RequestMapping("/measure/temperature")
	public ResponseMessage measureHitch(String companyId, String moduleId, HttpSession session) {
		if (null == moduleId || "".equals(moduleId)) {
			if (null == companyId || "".equals(companyId)) {
				companyId = userService.getCurrentUser(session).getCompanyId();
			}
			List<TemperatureMeasureHitchEventVO> eventList = measureEventService.selectMeasureHitch(companyId);
			return ResponseMessage.success(eventList);
		} else {
			List<TemperatureMeasureHitchEventVO> eventList = measureEventService.selectMeasureHitchByModuleId(moduleId);
			return ResponseMessage.success(eventList);
		}
	}
	
}
