package com.gdut.dongjun.web;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.po.Company;
import com.gdut.dongjun.po.Line;
import com.gdut.dongjun.po.Substation;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.HighVoltageSwitchService;
import com.gdut.dongjun.service.LineService;
import com.gdut.dongjun.service.SubstationService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.web.vo.ResponseMessage;

@Controller
@RequestMapping("/dongjun")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	@Autowired
	private LineService lineService;
	@Autowired
	private SubstationService substationService;
	@Autowired 
	private HighVoltageSwitchService switchService;
	
	@RequiresAuthentication
	@ResponseBody
	@RequestMapping("/company/list")
	public ResponseMessage list() {
		return ResponseMessage.success(companyService.selectByParameters(null));
	}
	
	@RequiresAuthentication
	@ResponseBody
	@RequestMapping("/company/edit")
	public ResponseMessage edit(Company c) {
		if (0 != companyService.updateByPrimaryKey(c)) {
			return ResponseMessage.success("success");
		}
		return ResponseMessage.warning("fail");
	}
	
	@RequiresAuthentication
	@ResponseBody
	@RequestMapping("/company/del")
	public ResponseMessage del(String companyId) {
		List<Substation> substations = substationService.selectByParameters(MyBatisMapUtil.warp("company_id", companyId));
		for (Substation sub : substations) {
			List<Line> lines = lineService.selectByParameters(MyBatisMapUtil.warp("substation_id", sub.getId()));
			for (Line l : lines) {
				switchService.deleteByParameters(MyBatisMapUtil.warp("line_id", sub.getId()));
				lineService.deleteByPrimaryKey(l.getId());
			}
			substationService.deleteByPrimaryKey(sub.getId());
		}
		companyService.deleteByPrimaryKey(companyId);
		return ResponseMessage.success("success");
	}
	
	@RequiresAuthentication
	@ResponseBody
	@RequestMapping("/line/list")
	public ResponseMessage listLine(String substationId) {
		return ResponseMessage.success(lineService.selectByParameters(MyBatisMapUtil.warp("substation_id", substationId)));
	}
	
	@RequiresAuthentication
	@ResponseBody
	@RequestMapping("/substation/list")
	public ResponseMessage listSubstation(String companyId) {
		return ResponseMessage.success(substationService.selectByParameters(MyBatisMapUtil.warp("company_id", companyId)));
	}
	
	@RequiresAuthentication
	@ResponseBody
	@RequestMapping("/switch/list")
	public ResponseMessage listSwitch(String lineId) {
		return ResponseMessage.success(switchService.selectByParameters(MyBatisMapUtil.warp("line_id", lineId)));
	}
	
	
	
	
}
