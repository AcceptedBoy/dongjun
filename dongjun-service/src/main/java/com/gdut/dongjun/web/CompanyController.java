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
import com.gdut.dongjun.util.UUIDUtil;
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
		if (null == c) {
			return ResponseMessage.warning("缺乏参数");
		}
		if (!isIpAddrAvailable(c.getIpAddr())) {
			return ResponseMessage.warning("ip地址不规范"); 
		}
		//	新增
		if (null == c.getId()) {
			String ipAddr = c.getIpAddr();
			List<Company> list = companyService.selectByParameters(MyBatisMapUtil.warp("ip_addr", ipAddr));
			if (null == list || list.size() == 0) {
				c.setId(UUIDUtil.getUUID());
				companyService.insert(c);
			} else {
				return ResponseMessage.warning("ip地址已被占用");  
			}
		}
		//	更新
		else {
			String ipAddr = c.getIpAddr();
			List<Company> list = companyService.selectByParameters(MyBatisMapUtil.warp("ip_addr", ipAddr));
			if (null == list || list.size() == 0 || list.get(0).getId().equals(c.getId())) {
				companyService.updateByPrimaryKey(c);
			} else {
				return ResponseMessage.warning("ip地址已被占用");  
			}
		}
		return ResponseMessage.warning("操作成功");  
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
	
	private static boolean isIpAddrAvailable(String ip) {
		if (null == ip) {
			return false;
		}
		return ip.matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))");
	}
	
	
	
}
