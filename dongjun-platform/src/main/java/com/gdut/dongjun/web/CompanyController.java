package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.domain.po.DataMonitor;
import com.gdut.dongjun.domain.po.PlatformGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.authc.UserRole;
import com.gdut.dongjun.dto.DeviceForAuthDTO;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.PlatformGroupService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.UserRoleService;
import com.gdut.dongjun.service.device.DataMonitorService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;

@Controller
@RequestMapping("/dongjun/company")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	@Autowired
	private UserService userService; 
	@Autowired
	private UserRoleService urService;
	@Autowired
	private PlatformGroupService pgService;
	@Autowired
	private DataMonitorService monitorService;

//	@RequiresPermissions("platform_group_admin:edit")
	@RequestMapping("/edit")
	@ResponseBody
	public ResponseMessage edit(Company com) {
		if (null == com.getId() || "".equals(com.getId())) {
			com.setId(UUIDUtil.getUUID());
			/* 插入与Company相对应的PlatformGroup */
			PlatformGroup pg = new PlatformGroup();
			pg.setId(UUIDUtil.getUUID());
			pg.setGroupId("default");	//默认组别
			byte num = 0;
			pg.setIsDefault(num);
			pg.setName(com.getName());
			pg.setCompanyId(com.getId());
			pgService.updateByPrimaryKey(pg);
		}
		if (companyService.updateByPrimaryKeySelective(com) == 0) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success(com);
	}
	
	@RequiresPermissions("platform_group_admin:delete")
	@RequestMapping("/del")
	@ResponseBody
	public ResponseMessage del(Company com, HttpSession session) {
		//TODO
		if (userService.getCurrentUser(session).getCompanyId() != com.getId()) {
			return ResponseMessage.warning("该用户非公司相关人员");
		}
		if (companyService.deleteByPrimaryKey(com.getId()) == false) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequiresAuthentication
	@RequestMapping("/imformation")
	@ResponseBody
	public ResponseMessage getImformation(HttpSession session) {
		User user = userService.getCurrentUser(session);
		//TODO 以后直接删掉company改成platformGroup
		PlatformGroup pg = pgService.selectByPrimaryKey(user.getCompanyId());
		Company com = companyService.selectByPrimaryKey(pg.getCompanyId());
		return ResponseMessage.success(com);
	}
	
	@RequiresAuthentication
	@RequestMapping("/staff")
	@ResponseBody
	public ResponseMessage getStaffById(HttpSession session) {
		User user = userService.getCurrentUser(session);
		List<User> users = userService.selectByParameters(MyBatisMapUtil.warp("company_id", user.getCompanyId()));
		return ResponseMessage.success(users);
	}
	
	@RequiresPermissions("platform_group_admin:role")
	@RequestMapping("/authc_add")
	@ResponseBody
	public ResponseMessage addStaffAuthc(
							@RequestParam(required = true) String roleId, 
							@RequestParam(required = true) String userId) {
		UserRole key = new UserRole();
		key.setUserId(userId);
		key.setRoleId(roleId);
		if (urService.insert(key) == 0) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequiresPermissions("platform_group_admin:role")
	@RequiresAuthentication
	@RequestMapping("/authc_update")
	@ResponseBody
	public ResponseMessage updateStaffAuthc(
							@RequestParam(required = true) String roleId, 
							@RequestParam(required = true) String userId) {
		UserRole key = new UserRole();
		key.setUserId(userId);
		key.setRoleId(roleId);
		if (urService.updateByPrimaryKey(key) == 0) {
			return ResponseMessage.warning("操作失败");
		}
		return ResponseMessage.success("操作成功");
	}
	
	@RequiresPermissions("platform_group_admin:role")
	@RequiresAuthentication
	@RequestMapping("/authc_del")
	@ResponseBody
	public ResponseMessage doChangeStaffAuthc(
							@RequestParam(required = true) String userId) {
		if (!urService.deleteByPrimaryKey(userId)) {
			return ResponseMessage.warning("删除失败");
		}
		return ResponseMessage.success("删除成功");
	}
	
	/**
	 * 权限模块
	 * 这个是返回当前公司的设备，要改啊
	 * @param session
	 * @return
	 */
	@RequestMapping("/device")
	@ResponseBody
	public ResponseMessage getAllDevice(HttpSession session) {
		User user = userService.getCurrentUser(session);
		List<DataMonitor> monitors = monitorService.selectByParameters(MyBatisMapUtil.warp("group_id", user.getCompanyId()));
		return ResponseMessage.success(wrapIntoDTO(monitors));
	}
	
	private List<DeviceForAuthDTO> wrapIntoDTO(List<DataMonitor> devices) {
		List<DeviceForAuthDTO> dtos = new ArrayList<DeviceForAuthDTO>();
		for (DataMonitor device : devices) {
			DeviceForAuthDTO dto = new DeviceForAuthDTO();
			dto.setId(device.getId());
			dto.setName(device.getName());
			dtos.add(dto);
		}
		return dtos;
	}
	
}
