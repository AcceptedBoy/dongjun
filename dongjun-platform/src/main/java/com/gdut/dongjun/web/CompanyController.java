package com.gdut.dongjun.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.po.BigGroupMapping;
import com.gdut.dongjun.domain.po.Company;
import com.gdut.dongjun.domain.po.DeviceGroup;
import com.gdut.dongjun.domain.po.User;
import com.gdut.dongjun.domain.po.authc.Role;
import com.gdut.dongjun.domain.po.authc.UserRole;
import com.gdut.dongjun.service.BigGroupMappingService;
import com.gdut.dongjun.service.CompanyService;
import com.gdut.dongjun.service.DeviceGroupMappingService;
import com.gdut.dongjun.service.DeviceGroupService;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.authc.RoleService;
import com.gdut.dongjun.service.authc.UserRoleService;
import com.gdut.dongjun.util.MyBatisMapUtil;
import com.gdut.dongjun.util.UUIDUtil;
import com.gdut.dongjun.web.vo.DeviceForAuthVO;

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
	private RoleService roleService;
	@Autowired
	private DeviceGroupService dgService;
	@Autowired
	private DeviceGroupMappingService dgMappingService;
	@Autowired
	private BigGroupMappingService bgMappingService;

	@RequestMapping("/register")
	@ResponseBody
	public ResponseMessage edit(
			@ModelAttribute("company") Company com, BindingResult result1,
			@ModelAttribute("user") User user, BindingResult result2,
			@RequestParam(required=true) String bigGroupId) {
		// 注册用户并赋予公司管理员角色
		List<Role> roles = roleService.selectByParameters(null);
		com.setId(UUIDUtil.getUUID());
		user.setCompanyId(com.getId());
		user.setId(UUIDUtil.getUUID());
		com.setMainStaffId(user.getId());
		com.setDelTag(false);
		user.setDelTag(false);
		companyService.insert(com);
		userService.insert(user);
		
		UserRole ur = new UserRole();
		for (Role role : roles) {
			if ("company_admin".equals(role.getRole())) {
				ur.setRoleId(role.getId());
				break;
			}
		}
		ur.setUserId(user.getId());
		urService.insert(ur);
		//添加大组到公司的映射
		BigGroupMapping b = new BigGroupMapping();
		b.setId(UUIDUtil.getUUID());
		b.setCompanyId(com.getId());
		b.setGroupId(bigGroupId);
		bgMappingService.insert(b);
		return ResponseMessage.success("操作成功");
	}

//	@RequiresPermissions("/edit")
	@RequestMapping("/del")
	@ResponseBody
	public ResponseMessage del(@RequestParam(required=true) String id, HttpSession session) {
		List<DeviceGroup> dgList = dgService.selectByParameters(MyBatisMapUtil.warp("company_id", id));
		if (null == dgList || 0 == dgList.size()) {
			//删除大组和公司的映射
			bgMappingService.deleteByParameters(MyBatisMapUtil.warp("company_id", id));
			//假删除
			companyService.deleteWithTag(id);
			return ResponseMessage.success("操作成功"); 
		}
		return ResponseMessage.warning("操作失败，该公司尚有设备分组，请尝试删除设备分组后再删除公司");
	}

	@RequiresAuthentication
	@RequestMapping("/imformation")
	@ResponseBody
	public ResponseMessage getImformation(HttpSession session) {
		User user = userService.getCurrentUser(session);
		// TODO 以后直接删掉company改成platformGroup
		Company com = companyService.selectByPrimaryKeyNoDel(user.getCompanyId());
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

//	@RequiresPermissions("platform_group_admin:role")
//	@RequestMapping("/authc_add")
//	@ResponseBody
//	public ResponseMessage addStaffAuthc(@RequestParam(required = true) String roleId,
//			@RequestParam(required = true) String userId) {
//		UserRole key = new UserRole();
//		key.setUserId(userId);
//		key.setRoleId(roleId);
//		if (urService.insert(key) == 0) {
//			return ResponseMessage.warning("操作失败");
//		}
//		return ResponseMessage.success("操作成功");
//	}

//	@RequiresPermissions("platform_group_admin:role")
//	@RequiresAuthentication
//	@RequestMapping("/authc_update")
//	@ResponseBody
//	public ResponseMessage updateStaffAuthc(@RequestParam(required = true) String roleId,
//			@RequestParam(required = true) String userId) {
//		UserRole key = new UserRole();
//		key.setUserId(userId);
//		key.setRoleId(roleId);
//		if (urService.updateByPrimaryKey(key) == 0) {
//			return ResponseMessage.warning("操作失败");
//		}
//		return ResponseMessage.success("操作成功");
//	}

//	@RequiresPermissions("platform_group_admin:role")
//	@RequiresAuthentication
//	@RequestMapping("/authc_del")
//	@ResponseBody
//	public ResponseMessage doChangeStaffAuthc(@RequestParam(required = true) String userId) {
//		if (!urService.deleteByPrimaryKey(userId)) {
//			return ResponseMessage.warning("删除失败");
//		}
//		return ResponseMessage.success("删除成功");
//	}

	/**
	 * 权限模块 这个是返回当前公司的设备，要改啊
	 * 
	 * @param session
	 * @return
	 */
//	@RequestMapping("/device")
//	@ResponseBody
//	public ResponseMessage getAllDevice(HttpSession session) {
//		User user = userService.getCurrentUser(session);
//		List<DataMonitor> monitors = monitorService
//				.selectByParameters(MyBatisMapUtil.warp("group_id", user.getCompanyId()));
//		return ResponseMessage.success(wrapIntoDTO(monitors));
//	}

//	private List<DeviceForAuthVO> wrapIntoDTO(List<DataMonitor> devices) {
//		List<DeviceForAuthVO> dtos = new ArrayList<DeviceForAuthVO>();
//		for (DataMonitor device : devices) {
//			DeviceForAuthVO dto = new DeviceForAuthVO();
//			dto.setId(device.getId());
//			dto.setName(device.getName());
//			dtos.add(dto);
//		}
//		return dtos;
//	}

}
